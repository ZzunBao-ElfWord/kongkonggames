package com.kongling.iap.gateway;

import com.apple.itunes.storekit.client.APIError;
import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.migration.ReceiptUtility;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import com.kongling.iap.api.ApiException;
import com.kongling.iap.config.AppleIapProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("!local-mock")
public class OfficialAppleGateway implements AppleGateway {
    private static final Logger log = LoggerFactory.getLogger(OfficialAppleGateway.class);
    private final AppleIapProperties properties;
    private final ReceiptUtility receiptUtility = new ReceiptUtility();

    private AppStoreServerAPIClient productionClient;
    private AppStoreServerAPIClient sandboxClient;
    private SignedDataVerifier productionVerifier;
    private SignedDataVerifier sandboxVerifier;

    public OfficialAppleGateway(AppleIapProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void initialize() throws IOException {
        requireText(properties.getBundleId(), "APPLE_BUNDLE_ID");
        requireText(properties.getIssuerId(), "APPLE_ISSUER_ID");
        requireText(properties.getKeyId(), "APPLE_KEY_ID");
        requireText(properties.getPrivateKeyPath(), "APPLE_PRIVATE_KEY_PATH");
        if (properties.getAppAppleId() == null || properties.getAppAppleId() <= 0) {
            throw new IllegalStateException("APPLE_APP_ID must be configured with the numeric App Apple ID");
        }

        List<Path> rootPaths = properties.getRootCertificatePaths().stream()
                .filter(StringUtils::hasText)
                .map(Path::of)
                .collect(Collectors.toList());
        if (rootPaths.isEmpty()) {
            throw new IllegalStateException("APPLE_ROOT_CERT_PATHS must contain Apple's root certificate paths");
        }

        String privateKey = Files.readString(Path.of(properties.getPrivateKeyPath()), StandardCharsets.UTF_8);
        Set<byte[]> rootCertificates = new LinkedHashSet<>();
        for (Path rootPath : rootPaths) rootCertificates.add(Files.readAllBytes(rootPath));

        productionClient = client(privateKey, Environment.PRODUCTION);
        sandboxClient = client(privateKey, Environment.SANDBOX);
        productionVerifier = verifier(rootCertificates, Environment.PRODUCTION);
        sandboxVerifier = verifier(rootCertificates, Environment.SANDBOX);
    }

    @Override
    public AppleVerifiedTransaction verifyTransaction(AppleVerificationInput input) {
        if (StringUtils.hasText(input.getSignedTransactionInfo())) {
            return verifySignedTransactionInEitherEnvironment(input.getSignedTransactionInfo());
        }

        String transactionId = input.getTransactionId();
        if (!StringUtils.hasText(transactionId) && StringUtils.hasText(input.getReceiptData())) {
            try {
                transactionId = receiptUtility.extractTransactionIdFromAppReceipt(input.getReceiptData());
            } catch (IOException e) {
                throw new ApiException("INVALID_RECEIPT", "Apple receipt could not be read", HttpStatus.BAD_REQUEST);
            }
        }
        if (!StringUtils.hasText(transactionId)) {
            throw new ApiException("MISSING_TRANSACTION_ID", "transactionId or a readable receipt is required", HttpStatus.BAD_REQUEST);
        }

        if (properties.isSandboxOnly()) {
            try {
                return queryAndVerify(sandboxClient, sandboxVerifier, transactionId);
            } catch (APIException e) {
                throw appleApiFailure(e);
            } catch (IOException e) {
                throw appleUnavailable(e);
            }
        }

        try {
            return queryAndVerify(productionClient, productionVerifier, transactionId);
        } catch (APIException e) {
            if (e.getApiError() != APIError.TRANSACTION_ID_NOT_FOUND) throw appleApiFailure(e);
            try {
                return queryAndVerify(sandboxClient, sandboxVerifier, transactionId);
            } catch (APIException sandboxError) {
                throw appleApiFailure(sandboxError);
            } catch (IOException sandboxError) {
                throw appleUnavailable(sandboxError);
            }
        } catch (IOException e) {
            throw appleUnavailable(e);
        }
    }

    @Override
    public AppleNotificationData verifyNotification(String signedPayload) {
        ResponseBodyV2DecodedPayload decoded;
        SignedDataVerifier verifier;
        try {
            decoded = productionVerifier.verifyAndDecodeNotification(signedPayload);
            verifier = productionVerifier;
        } catch (VerificationException productionError) {
            try {
                decoded = sandboxVerifier.verifyAndDecodeNotification(signedPayload);
                verifier = sandboxVerifier;
            } catch (VerificationException sandboxError) {
                throw invalidSignedData("Apple notification signature is invalid", sandboxError);
            }
        }

        AppleNotificationData notification = new AppleNotificationData();
        notification.setNotificationUuid(decoded.getNotificationUUID());
        notification.setNotificationType(decoded.getNotificationType() == null
                ? decoded.getRawNotificationType() : decoded.getNotificationType().getValue());
        notification.setSubtype(decoded.getSubtype() == null
                ? decoded.getRawSubtype() : decoded.getSubtype().getValue());
        Data data = decoded.getData();
        if (data != null) {
            notification.setEnvironment(data.getEnvironment() == null
                    ? data.getRawEnvironment() : data.getEnvironment().getValue());
            if (StringUtils.hasText(data.getSignedTransactionInfo())) {
                try {
                    notification.setTransaction(map(
                            verifier.verifyAndDecodeTransaction(data.getSignedTransactionInfo()),
                            data.getSignedTransactionInfo()));
                } catch (VerificationException e) {
                    throw invalidSignedData("Notification transaction signature is invalid", e);
                }
            }
        }
        return notification;
    }

    private AppleVerifiedTransaction queryAndVerify(AppStoreServerAPIClient client,
                                                     SignedDataVerifier verifier,
                                                     String transactionId) throws APIException, IOException {
        TransactionInfoResponse response = client.getTransactionInfo(transactionId);
        try {
            return map(verifier.verifyAndDecodeTransaction(response.getSignedTransactionInfo()),
                    response.getSignedTransactionInfo());
        } catch (VerificationException e) {
            throw invalidSignedData("Apple transaction signature is invalid", e);
        }
    }

    private AppleVerifiedTransaction verifySignedTransactionInEitherEnvironment(String signedTransaction) {
        try {
            return map(productionVerifier.verifyAndDecodeTransaction(signedTransaction), signedTransaction);
        } catch (VerificationException productionError) {
            try {
                return map(sandboxVerifier.verifyAndDecodeTransaction(signedTransaction), signedTransaction);
            } catch (VerificationException sandboxError) {
                throw invalidSignedData("Apple transaction signature is invalid", sandboxError);
            }
        }
    }

    private AppleVerifiedTransaction map(JWSTransactionDecodedPayload payload, String signedTransaction) {
        AppleVerifiedTransaction result = new AppleVerifiedTransaction();
        result.setTransactionId(payload.getTransactionId());
        result.setOriginalTransactionId(payload.getOriginalTransactionId());
        result.setProductId(payload.getProductId());
        result.setBundleId(payload.getBundleId());
        result.setEnvironment(payload.getEnvironment() == null
                ? payload.getRawEnvironment() : payload.getEnvironment().getValue());
        result.setPurchaseDate(instant(payload.getPurchaseDate()));
        result.setExpiresDate(instant(payload.getExpiresDate()));
        result.setRevocationDate(instant(payload.getRevocationDate()));
        result.setQuantity(payload.getQuantity() == null ? 1 : payload.getQuantity());
        result.setSignedTransaction(signedTransaction);
        return result;
    }

    private AppStoreServerAPIClient client(String privateKey, Environment environment) {
        return new AppStoreServerAPIClient(
                privateKey,
                properties.getKeyId(),
                properties.getIssuerId(),
                properties.getBundleId(),
                environment);
    }

    private SignedDataVerifier verifier(Set<byte[]> roots, Environment environment) {
        Set<InputStream> streams = roots.stream()
                .map(ByteArrayInputStream::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new SignedDataVerifier(
                streams,
                properties.getBundleId(),
                properties.getAppAppleId(),
                environment,
                true);
    }

    private ApiException appleApiFailure(APIException e) {
        log.warn("Apple Server API rejected request: httpStatus={}, apiError={}, rawApiError={}, message={}",
                e.getHttpStatusCode(), e.getApiError(), e.getRawApiError(), e.getApiErrorMessage());
        if (e.getApiError() == APIError.TRANSACTION_ID_NOT_FOUND) {
            return new ApiException("TRANSACTION_NOT_FOUND", "Apple transaction was not found", HttpStatus.NOT_FOUND);
        }
        return new ApiException("APPLE_API_ERROR", "Apple verification service rejected the request", HttpStatus.BAD_GATEWAY);
    }

    private ApiException appleUnavailable(Exception e) {
        return new ApiException("APPLE_UNAVAILABLE", "Apple verification service is temporarily unavailable", HttpStatus.BAD_GATEWAY);
    }

    private ApiException invalidSignedData(String message, Exception e) {
        return new ApiException("INVALID_APPLE_SIGNATURE", message, HttpStatus.BAD_REQUEST);
    }

    private Instant instant(Long epochMilliseconds) {
        return epochMilliseconds == null ? null : Instant.ofEpochMilli(epochMilliseconds);
    }

    private void requireText(String value, String name) {
        if (!StringUtils.hasText(value)) throw new IllegalStateException(name + " must be configured");
    }
}

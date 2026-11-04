package com.kongling.iap.gateway;

import com.kongling.iap.api.ApiException;
import com.kongling.iap.config.AppleIapProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@Profile("local-mock")
public class MockAppleGateway implements AppleGateway {
    private final AppleIapProperties properties;

    public MockAppleGateway(AppleIapProperties properties) {
        this.properties = properties;
    }

    @Override
    public AppleVerifiedTransaction verifyTransaction(AppleVerificationInput input) {
        if (!StringUtils.hasText(input.getTransactionId())) {
            throw new ApiException("MISSING_TRANSACTION_ID", "local mock requires transactionId", HttpStatus.BAD_REQUEST);
        }
        AppleVerifiedTransaction transaction = new AppleVerifiedTransaction();
        transaction.setTransactionId(input.getTransactionId());
        transaction.setOriginalTransactionId("original-" + input.getTransactionId());
        transaction.setProductId(input.getExpectedProductId());
        transaction.setBundleId(properties.getBundleId());
        transaction.setEnvironment("Sandbox");
        transaction.setPurchaseDate(Instant.now());
        if (input.getExpectedProductId().matches("000[123]")) {
            transaction.setExpiresDate(Instant.now().plus(30, ChronoUnit.DAYS));
        }
        transaction.setQuantity(1);
        transaction.setSignedTransaction("LOCAL_MOCK:" + input.getTransactionId());
        return transaction;
    }

    @Override
    public AppleNotificationData verifyNotification(String signedPayload) {
        if (!signedPayload.startsWith("LOCAL_MOCK_NOTIFICATION:")) {
            throw new ApiException("INVALID_MOCK_NOTIFICATION", "invalid local mock notification", HttpStatus.BAD_REQUEST);
        }
        String[] fields = signedPayload.split(":", 5);
        AppleNotificationData notification = new AppleNotificationData();
        notification.setNotificationUuid(fields.length > 1 && StringUtils.hasText(fields[1]) ? fields[1] : UUID.randomUUID().toString());
        notification.setNotificationType(fields.length > 2 ? fields[2] : "TEST");
        notification.setSubtype(null);
        notification.setEnvironment("Sandbox");
        if (fields.length == 5) {
            AppleVerifiedTransaction transaction = new AppleVerifiedTransaction();
            transaction.setTransactionId(fields[4]);
            transaction.setOriginalTransactionId("original-" + fields[4]);
            transaction.setProductId(fields[3]);
            transaction.setBundleId(properties.getBundleId());
            transaction.setEnvironment("Sandbox");
            transaction.setPurchaseDate(Instant.now());
            notification.setTransaction(transaction);
        }
        return notification;
    }
}

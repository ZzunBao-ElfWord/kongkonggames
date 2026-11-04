package com.kongling.iap.service;

import com.kongling.iap.api.ApiException;
import com.kongling.iap.api.VerifyTransactionRequest;
import com.kongling.iap.api.VerifyTransactionResponse;
import com.kongling.iap.config.AppleIapProperties;
import com.kongling.iap.domain.ProductDefinition;
import com.kongling.iap.domain.ProductType;
import com.kongling.iap.gateway.AppleGateway;
import com.kongling.iap.gateway.AppleVerificationInput;
import com.kongling.iap.gateway.AppleVerifiedTransaction;
import com.kongling.iap.persistence.AppleTransactionRepository;
import com.kongling.iap.persistence.StoredTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
public class TransactionVerificationService {
    private final AppleGateway appleGateway;
    private final ProductCatalog productCatalog;
    private final AppleTransactionRepository repository;
    private final AppleIapProperties properties;

    public TransactionVerificationService(AppleGateway appleGateway,
                                          ProductCatalog productCatalog,
                                          AppleTransactionRepository repository,
                                          AppleIapProperties properties) {
        this.appleGateway = appleGateway;
        this.productCatalog = productCatalog;
        this.repository = repository;
        this.properties = properties;
    }

    @Transactional
    public VerifyTransactionResponse verify(VerifyTransactionRequest request) {
        if (!StringUtils.hasText(request.getTransactionId())
                && !StringUtils.hasText(request.getReceiptData())
                && !StringUtils.hasText(request.getSignedTransactionInfo())) {
            throw new ApiException("MISSING_APPLE_PROOF", "transactionId, receiptData or signedTransactionInfo is required", HttpStatus.BAD_REQUEST);
        }

        ProductDefinition product = productCatalog.find(request.getProductId())
                .orElseThrow(() -> new ApiException("UNKNOWN_PRODUCT", "productId is not allowed", HttpStatus.BAD_REQUEST));

        AppleVerifiedTransaction transaction = appleGateway.verifyTransaction(new AppleVerificationInput(
                request.getProductId(),
                request.getTransactionId(),
                request.getReceiptData(),
                request.getSignedTransactionInfo()
        ));
        validateVerifiedTransaction(request, product, transaction);

        boolean firstProcessed = repository.insertVerified(request.getUserId(), product, transaction);
        StoredTransaction stored = repository.find(transaction.getTransactionId())
                .orElseThrow(() -> new ApiException("STORE_FAILED", "verified transaction could not be stored", HttpStatus.INTERNAL_SERVER_ERROR));
        if (stored.getUserId() != request.getUserId()) {
            throw new ApiException("TRANSACTION_OWNED_BY_ANOTHER_USER", "transaction is already bound to another user", HttpStatus.CONFLICT);
        }
        if (!stored.getProductId().equals(product.getProductId())) {
            throw new ApiException("TRANSACTION_PRODUCT_CONFLICT", "transaction is already bound to another product", HttpStatus.CONFLICT);
        }

        VerifyTransactionResponse response = new VerifyTransactionResponse();
        response.setValid(true);
        response.setFirstProcessed(firstProcessed);
        response.setGrantRequired("VERIFIED_PENDING_GRANT".equals(stored.getStatus()));
        response.setStatus(stored.getStatus());
        response.setTransactionId(transaction.getTransactionId());
        response.setOriginalTransactionId(transaction.getOriginalTransactionId());
        response.setProductId(transaction.getProductId());
        response.setProductType(product.getType().name());
        response.setGrantValue(product.getGrantValue());
        response.setEnvironment(transaction.getEnvironment());
        response.setPurchaseDate(transaction.getPurchaseDate());
        response.setExpiresDate(transaction.getExpiresDate());
        return response;
    }

    @Transactional
    public void acknowledgeGrant(String transactionId, long userId, String businessOrderNo) {
        StoredTransaction transaction = repository.find(transactionId)
                .orElseThrow(() -> new ApiException("TRANSACTION_NOT_FOUND", "verified transaction does not exist", HttpStatus.NOT_FOUND));
        if (transaction.getUserId() != userId) {
            throw new ApiException("TRANSACTION_OWNED_BY_ANOTHER_USER", "transaction belongs to another user", HttpStatus.CONFLICT);
        }
        if (!repository.markGranted(transactionId, userId, businessOrderNo)) {
            throw new ApiException("ACK_FAILED", "unable to acknowledge entitlement grant", HttpStatus.CONFLICT);
        }
    }

    private void validateVerifiedTransaction(VerifyTransactionRequest request,
                                             ProductDefinition product,
                                             AppleVerifiedTransaction transaction) {
        if (!StringUtils.hasText(transaction.getTransactionId())) {
            throw new ApiException("INVALID_APPLE_TRANSACTION", "Apple transaction has no transactionId", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!properties.getBundleId().equals(transaction.getBundleId())) {
            throw new ApiException("BUNDLE_ID_MISMATCH", "Apple transaction belongs to another app", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!product.getProductId().equals(transaction.getProductId())) {
            throw new ApiException("PRODUCT_ID_MISMATCH", "purchased product does not match requested product", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.hasText(request.getTransactionId())
                && !request.getTransactionId().equals(transaction.getTransactionId())) {
            throw new ApiException("TRANSACTION_ID_MISMATCH", "Apple returned another transactionId", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (transaction.getRevocationDate() != null) {
            throw new ApiException("TRANSACTION_REVOKED", "Apple transaction has been revoked", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (product.getType() == ProductType.MEMBERSHIP
                && (transaction.getExpiresDate() == null || !transaction.getExpiresDate().isAfter(Instant.now()))) {
            throw new ApiException("SUBSCRIPTION_EXPIRED", "subscription is not active", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

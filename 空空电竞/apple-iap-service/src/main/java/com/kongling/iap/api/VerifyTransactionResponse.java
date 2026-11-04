package com.kongling.iap.api;

import java.time.Instant;

public class VerifyTransactionResponse {
    private boolean valid;
    private boolean firstProcessed;
    private boolean grantRequired;
    private String status;
    private String transactionId;
    private String originalTransactionId;
    private String productId;
    private String productType;
    private int grantValue;
    private String environment;
    private Instant purchaseDate;
    private Instant expiresDate;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public boolean isFirstProcessed() { return firstProcessed; }
    public void setFirstProcessed(boolean firstProcessed) { this.firstProcessed = firstProcessed; }
    public boolean isGrantRequired() { return grantRequired; }
    public void setGrantRequired(boolean grantRequired) { this.grantRequired = grantRequired; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getOriginalTransactionId() { return originalTransactionId; }
    public void setOriginalTransactionId(String originalTransactionId) { this.originalTransactionId = originalTransactionId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public int getGrantValue() { return grantValue; }
    public void setGrantValue(int grantValue) { this.grantValue = grantValue; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public Instant getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Instant purchaseDate) { this.purchaseDate = purchaseDate; }
    public Instant getExpiresDate() { return expiresDate; }
    public void setExpiresDate(Instant expiresDate) { this.expiresDate = expiresDate; }
}

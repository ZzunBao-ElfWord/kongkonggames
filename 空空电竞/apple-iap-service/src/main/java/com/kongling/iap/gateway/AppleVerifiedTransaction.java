package com.kongling.iap.gateway;

import java.time.Instant;

public class AppleVerifiedTransaction {
    private String transactionId;
    private String originalTransactionId;
    private String productId;
    private String bundleId;
    private String environment;
    private Instant purchaseDate;
    private Instant expiresDate;
    private Instant revocationDate;
    private int quantity = 1;
    private String signedTransaction;

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getOriginalTransactionId() { return originalTransactionId; }
    public void setOriginalTransactionId(String originalTransactionId) { this.originalTransactionId = originalTransactionId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getBundleId() { return bundleId; }
    public void setBundleId(String bundleId) { this.bundleId = bundleId; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public Instant getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Instant purchaseDate) { this.purchaseDate = purchaseDate; }
    public Instant getExpiresDate() { return expiresDate; }
    public void setExpiresDate(Instant expiresDate) { this.expiresDate = expiresDate; }
    public Instant getRevocationDate() { return revocationDate; }
    public void setRevocationDate(Instant revocationDate) { this.revocationDate = revocationDate; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getSignedTransaction() { return signedTransaction; }
    public void setSignedTransaction(String signedTransaction) { this.signedTransaction = signedTransaction; }
}

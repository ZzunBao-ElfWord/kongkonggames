package com.kongling.iap.persistence;

public class StoredTransaction {
    private final String transactionId;
    private final long userId;
    private final String productId;
    private final String status;

    public StoredTransaction(String transactionId, long userId, String productId, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.productId = productId;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public long getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getStatus() { return status; }
}

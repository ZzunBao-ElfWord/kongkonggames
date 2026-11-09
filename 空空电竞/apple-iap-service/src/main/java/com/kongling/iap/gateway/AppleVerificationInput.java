package com.kongling.iap.gateway;

public class AppleVerificationInput {
    private final String expectedProductId;
    private final String transactionId;
    private final String receiptData;
    private final String signedTransactionInfo;

    public AppleVerificationInput(String expectedProductId, String transactionId, String receiptData, String signedTransactionInfo) {
        this.expectedProductId = expectedProductId;
        this.transactionId = transactionId;
        this.receiptData = receiptData;
        this.signedTransactionInfo = signedTransactionInfo;
    }

    public String getExpectedProductId() { return expectedProductId; }
    public String getTransactionId() { return transactionId; }
    public String getReceiptData() { return receiptData; }
    public String getSignedTransactionInfo() { return signedTransactionInfo; }
}

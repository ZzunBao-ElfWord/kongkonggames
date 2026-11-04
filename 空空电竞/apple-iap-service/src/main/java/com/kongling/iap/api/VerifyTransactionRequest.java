package com.kongling.iap.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VerifyTransactionRequest {
    @NotNull
    private Long userId;
    @NotBlank
    private String productId;
    private String transactionId;
    private String receiptData;
    private String signedTransactionInfo;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getReceiptData() { return receiptData; }
    public void setReceiptData(String receiptData) { this.receiptData = receiptData; }
    public String getSignedTransactionInfo() { return signedTransactionInfo; }
    public void setSignedTransactionInfo(String signedTransactionInfo) { this.signedTransactionInfo = signedTransactionInfo; }
}

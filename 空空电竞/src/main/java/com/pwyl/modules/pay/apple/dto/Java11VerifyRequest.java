package com.pwyl.modules.pay.apple.dto;

import lombok.Data;

/**
 * 调用 Java11 验证服务 /internal/v1/apple/transactions/verify 的请求体。
 */
@Data
public class Java11VerifyRequest {
    private Long userId;
    private String productId;
    private String transactionId;
    private String receiptData;
    private String signedTransactionInfo;
}

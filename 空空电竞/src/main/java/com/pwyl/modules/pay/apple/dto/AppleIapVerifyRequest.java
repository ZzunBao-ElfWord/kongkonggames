package com.pwyl.modules.pay.apple.dto;

import lombok.Data;

/**
 * App 端上送的苹果内购验证请求。
 * transactionIdentifier 对应 transactionId；
 * transactionReceipt 对应 receiptData；
 * StoreKit2 场景可额外上送 JWS(signedTransactionInfo)。
 */
@Data
public class AppleIapVerifyRequest {
    /** 苹果商品 id */
    private String productId;
    /** 苹果交易唯一标识(必传，DCloud 回调字段 transactionIdentifier) */
    private String transactionId;
    /** 支付票据 base64(DCloud 回调字段 transactionReceipt)，可选 */
    private String receiptData;
    /** StoreKit2 JWS 签名交易，可选 */
    private String signedTransactionInfo;
}

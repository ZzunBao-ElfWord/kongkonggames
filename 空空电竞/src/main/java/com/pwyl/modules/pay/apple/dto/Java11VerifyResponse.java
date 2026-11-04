package com.pwyl.modules.pay.apple.dto;

import lombok.Data;

/**
 * Java11 验证服务返回的标准化交易结果。
 * 时间字段为苹果返回的 ISO-8601 Instant 字符串(如 2026-09-30T03:00:00Z)，
 * 用 String 接收避免 Jackson 在 Java8 下对 Instant 的反序列化问题，由业务侧统一转时区。
 */
@Data
public class Java11VerifyResponse {
    /** 交易是否合法有效 */
    private boolean valid;
    /** 是否为 Java11 侧第一次处理该交易 */
    private boolean firstProcessed;
    /** 是否需要业务侧发放权益(Java11 状态为 VERIFIED_PENDING_GRANT) */
    private boolean grantRequired;
    /** Java11 侧交易状态 */
    private String status;
    private String transactionId;
    private String originalTransactionId;
    private String productId;
    /** 商品类型：COIN / MEMBERSHIP */
    private String productType;
    /** 发放值：柚币数量或会员月数 */
    private int grantValue;
    /** 环境：Sandbox / Production */
    private String environment;
    /** 购买时间(ISO-8601) */
    private String purchaseDate;
    /** 订阅到期时间(ISO-8601)，非续期会员/消耗品可能为空 */
    private String expiresDate;
}

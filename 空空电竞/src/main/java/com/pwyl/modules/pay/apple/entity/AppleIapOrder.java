package com.pwyl.modules.pay.apple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 苹果内购订单。
 * status: 0待验证 1验证中 2验证失败(终态) 3待发货 4已发货待ack 5已完成(终态) 6已退款
 * ackStatus: 0待处理 1成功 2失败待重试
 * bizType: 1柚币 2会员
 */
@Data
@TableName("apple_iap_order")
public class AppleIapOrder {

    public static final int BIZ_COIN = 1;
    public static final int BIZ_MEMBERSHIP = 2;

    public static final int STATUS_INIT = 0;
    public static final int STATUS_VERIFYING = 1;
    public static final int STATUS_VERIFY_FAILED = 2;
    public static final int STATUS_PENDING_DELIVER = 3;
    public static final int STATUS_DELIVERED = 4;
    public static final int STATUS_ACK_SUCCESS = 5;
    public static final int STATUS_REFUNDED = 6;

    public static final int ACK_PENDING = 0;
    public static final int ACK_SUCCESS = 1;
    public static final int ACK_RETRY = 2;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String transactionId;
    private String originalTransactionId;
    private String productId;
    private Integer bizType;
    private BigDecimal coinAmount;
    private Integer vipNameType;
    private String vipEndTime;
    private Integer grantValue;
    private String purchaseTime;
    private String expiresTime;
    private String environment;
    private Integer status;
    private String verifyFailReason;
    private String receiptData;
    private String signedTransaction;
    private String rawResponse;
    private String businessOrderNo;
    private Integer ackStatus;
    private Integer ackRetryCount;
    private String ackTime;
    private String verifyTime;
    private String deliverTime;
    private String createTime;
    private String updateTime;
}

package com.pwyl.modules.orders.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MyOrderResponse implements Serializable {
    /**
     * 订单状态0待支付1进行中2已完成3已退款
     */
    private String state;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 接单id
      */
    private Long orderTakingId;
    /**
     *接单用户id
     */
    private Long userId;
    /**
     * 头像
      */
    private String avatar;
    /**
     * 游戏名称
     */
    private String userName;
    /**
     *游戏名称
     */
    private String gameName;
    /**
     * 数量
      */
    private int orderNumber;
    /**
     * 价格
     */
    private Double payMoney;
    /**
     * 订单id
     */
    private Long  ordersId;
    private String gameImg;


    private Integer commentCount;
    private Integer classify;
    private String unit;

    private BigDecimal rate;
    private BigDecimal zhiRate;
    private Long zhiUserId;
    private BigDecimal feiRate;
    private Long feiUserId;
    private BigDecimal pingRate;

    private Long couponId;
    private BigDecimal couponMoney;
    private Integer orderType;
    private BigDecimal consortiaRate;
    private BigDecimal consortiaUserId;

}

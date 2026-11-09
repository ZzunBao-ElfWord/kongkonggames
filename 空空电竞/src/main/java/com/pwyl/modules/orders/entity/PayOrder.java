package com.pwyl.modules.orders.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liyuan
 * @description order
 * @date 2021-08-24
 */
@Data
@ApiModel("pay_order")
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    /**
     * 订单标号
     */
    @ApiModelProperty("订单标号")
    private String ordersNo;

    /**
     * 支付宝支付单号
     */
    @ApiModelProperty("支付宝支付单号")
    private String tradeNo;

    /**
     * 金币金额
     */
    @ApiModelProperty("金币金额")
    private BigDecimal money;

    @ApiModelProperty("赠送金币")
    private BigDecimal giveMoney;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;

    /**
     * 支付方式 1微信小程序 2微信公众号 3微信app 4支付宝
     */
    @ApiModelProperty("支付方式 1微信小程序 2微信公众号 3微信app 4支付宝")
    private Integer payWay;

    /**
     * 状态 0待支付 1已支付 2已退款
     */
    @ApiModelProperty("状态 0待支付 1已支付 2已退款")
    private Integer state;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 退款原因
     */
    @ApiModelProperty("退款原因")
    private String refundContent;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String updateTime;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    public PayOrder() {
    }
}
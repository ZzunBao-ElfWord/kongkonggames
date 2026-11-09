package com.pwyl.modules.orders.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("订单表")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Orders implements Serializable {
    @ApiModelProperty("订单id")
    @TableId(type = IdType.AUTO)
    private Long ordersId;
    @ApiModelProperty("订单编号")
    private String ordersNo;
    private Long orderTakingUserId;
    private BigDecimal orderTakingMoney;
    @ApiModelProperty("用户id")
    private Long userId;
    @TableField(exist = false)
    private UserEntity user;
    @TableField(exist = false)
    private UserEntity ordersUser;
    @ApiModelProperty("接单id")
    private Long orderTakingId;
    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;
    @ApiModelProperty("订单状态0待支付1待服务2已完成3已退款4进行中")
    private String state;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("退款原因")
    private String refundContent;
    @ApiModelProperty("订单种类1接单2会员")
    private Long ordersType;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("订单数量")
    private Long orderNumber;
    @ApiModelProperty("评分")
    private Double orderScore;
    @ApiModelProperty("修改时间")
    private String updateTime;
    @ApiModelProperty("会员类型id")
    private Long vipDetailsId;
    @ApiModelProperty("用户类型")
    private Long type;
    @ApiModelProperty("假删除")
    private Long isdelete;
    @ApiModelProperty("技师佣金")
    private BigDecimal rate;
    @ApiModelProperty("一级佣金")
    private BigDecimal zhiRate;
    private Long zhiUserId;
    @ApiModelProperty("二级佣金")
    private BigDecimal feiRate;
    private Long feiUserId;
    @ApiModelProperty("平台利润")
    private BigDecimal pingRate;
    @ApiModelProperty("优惠券id")
    private Long couponId;
    @ApiModelProperty("优惠金额")
    private BigDecimal couponMoney;
    private String endTime;
    private Long fastOrderId;
    private Long fastOrderTakeId;
    private BigDecimal consortiaRate;
    private Long consortiaUserId;
    /**
     * 订单类型 1下单 2约玩
     */
    private Integer orderType;
    @TableField(exist = false)
    private Game game;
    /**
     * 单价
     */
    @TableField(exist = false)
    private BigDecimal money;
    @TableField(exist = false)
    private OrderTaking orderTaking;

}

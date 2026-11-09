package com.pwyl.modules.orders.response;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderAllResponse implements Serializable {

    @ApiModelProperty("订单id")
    private Long ordersId;
    @ApiModelProperty("订单编号")
    private String ordersNo;
    @ApiModelProperty("接单id")
    private Long orderTakingId;
    @ApiModelProperty("支付金额")
    private BigDecimal payMoney;
    @ApiModelProperty("订单状态0待支付1进行中2已完成3已退款")
    private Long state;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("订单种类1接单2会员")
    private Long ordersType;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("订单数量")
    private Long orderNumber;
    @ApiModelProperty("会员类型id")
    private Long vipDetailsId;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String avatar;
    @ApiModelProperty("评分")
    private Double orderScore;
    private Long userId;
    private BigDecimal memberMoney;
    private BigDecimal money;
    private BigDecimal oldMoney;
    private Integer classify;
    private String unit;
    private String ordersUserName;
    private String ordersUserAvatar;
    private Long ordersUserId;

    private BigDecimal rate;
    private BigDecimal zhiRate;
    private Long zhiUserId;
    private BigDecimal feiRate;
    private Long feiUserId;
    private BigDecimal pingRate;

    private String zhiUserName;
    private String feiUserName;

    private Integer orderType;
    private Long couponId;
    private BigDecimal couponMoney;
    private BigDecimal consortiaRate;
    private BigDecimal consortiaUserId;
}

package com.pwyl.modules.gift.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description gift_user
 * @author fang
 * @date 2022-03-31
 */
@Data
public class GiftUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 用户礼物id
     */
    private Long giftUserId;

    /**
     * 礼物id
     */
    private Long giftId;

    /**
     * 礼物金额
     */
    private BigDecimal giftMoney;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物图片
     */
    private String giftImg;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 赠送用户id
     */
    private Long sendUserId;

    /**
     * 时间
     */
    private String createTime;

    private BigDecimal userMoney;

    private BigDecimal consortiaRate;

    private Long consortiaUserId;

    private BigDecimal pingMoney;

    private Long roomUserId;

    private Long roomId;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String sendUserName;

    @TableField(exist = false)
    private String sendPhone;

    @TableField(exist = false)
    private Long talkId;

    @TableField(exist = false)
    private Long orderTakingId;


    public GiftUser() {}
}

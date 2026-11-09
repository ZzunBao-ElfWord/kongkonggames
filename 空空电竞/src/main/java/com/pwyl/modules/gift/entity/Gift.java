package com.pwyl.modules.gift.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description gift
 * @author fang
 * @date 2022-03-31
 */
@Data
public class Gift implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 礼物id
     */
    private Long giftId;

    /**
     * 礼物图片
     */
    private String giftImg;

    /**
     * 礼物特效
     */
    private String giftGif;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物价格
     */
    private BigDecimal giftMoney;

    /**
     * 状态 1正常 2下架
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 时间
     */
    private String createTime;

    public Gift() {}
}

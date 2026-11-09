package com.pwyl.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description pay_classify
 * @author fang
 * @date 2022-04-06
 */
@Data
public class PayClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 充值分类id
     */
    private Long payClassifyId;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 金币数量
     */
    private BigDecimal money;

    private BigDecimal giveMoney;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 时间
     */
    private String createTime;

    public PayClassify() {}
}

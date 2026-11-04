package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author fang
 * @date 2021/12/27
 */
@Data
public class OrderTakingReward {
    private static final long serialVersionUID = 1L;

    /**
     * 打赏id
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long rewardId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 动态id
     */
    private Long orderTakingId;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 时间
     */
    private String createTime;

}
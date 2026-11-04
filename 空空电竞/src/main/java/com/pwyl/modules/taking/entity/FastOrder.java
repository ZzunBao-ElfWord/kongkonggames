package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description fast_order
 * @author fang
 * @date 2022-04-20
 */
@Data
public class FastOrder implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 快速越玩id
     */
    @TableId(type = IdType.AUTO)
    private Long fastOrderId;

    /**
     * 游戏id
     */
    private Long gameId;

    @TableField(exist = false)
    private String gameName;

    @TableField(exist = false)
    private Game game;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 性别要求
     */
    private Integer sex;

    /**
     * 单价区间
     */
    private String money;

    /**
     * 技能水平
     */
    private String skill;

    /**
     * 期望ta擅长的位置
     */
    private String location;

    /**
     * 期望ta擅长的英雄
     */
    private String hero;

    /**
     * 期望ta包上分段位
     */
    private String orderLevel;

    /**
     * 玩家当前段位
     */
    private String myLevel;

    /**
     * 玩家系统
     */
    private String mySystem;

    /**
     * 玩家大区
     */
    private String myArea;

    /**
     * 约完局数
     */
    private Integer num;

    /**
     * 陪玩时间
     */
    private String orderTakingTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 1发起中 2匹配成功  3取消约玩
     */
    private Integer status;

    /**
     * 接单用户id
     */
    private Long ordersUserId;

    /**
     * 订单id
     */
    private Long ordersId;

    private BigDecimal fastMoney;

    @TableField(exist = false)
    private BigDecimal minMoney;

    @TableField(exist = false)
    private BigDecimal maxMoney;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String ordersUserName;

    @TableField(exist = false)
    private String ordersPhone;

    @TableField(exist = false)
    private String content;

    @TableField(exist = false)
    private String voice;


}

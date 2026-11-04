package com.pwyl.modules.taking.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 接单详情返回字段
 */
@Data
public class TakingDetailsResponse implements Serializable {


    private Long id;
    /**
     *发布用户id
     */
    private Long userId;
    /**
     * 主页显示图片
     */
    private String homepageImg;
    /**
     * 用户图像
     */
    private String avatar;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 城市
     */
    private String city;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 性别 1男 2女
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 价格
     */
    private Double money;
    /**
     * 单位
     */
    private String unitType;
    /**
     * 评分
     */
    private Double orderScore;
    /**
     * 下单
     */
    private int count;
    /**
     * 语音介绍
     */
    private String voiceIntroduce;
    /**
     * 接单大区
     */
    private String orderTakingArea;
    /**
     * 接单时间
     */
    private String orderTakingTime;
    private BigDecimal oldMoney;
    private BigDecimal memberMoney;
    private String gameImg;
    private Integer sec;
    private String createTime;
    private String myLevel;
    private Integer classify;
    private String unit;
    private Integer rewardCount;
    private Integer myRewardCount;
    private Integer distance;
    private String status;
}

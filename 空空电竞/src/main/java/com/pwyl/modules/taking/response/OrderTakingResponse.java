package com.pwyl.modules.taking.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderTakingResponse implements Serializable {
    /**
     * 接单id
     */
    private Long id;
    /**
     * 发布人id
     */
    private Long userId;

    /**
     * 发布人姓名
     */
    private String userName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 发布城市
     */
    private String city;
    /**
     * 发布人图像
     */
    private String avatar;
    /**
     * 接单游戏
     */
    private String gameName;
    /**
     * 我的段位
     */
    private String myLevel;
    /**
     * 订单评分
     */
    private Double orderScore;
    /**
     * 价钱
     */
    private BigDecimal money;
    /**
     * 服务人数
     */
    private  int  count;
    private BigDecimal oldMoney;
    private BigDecimal memberMoney;
    private String gameImg;
    private Integer sec;
    private Integer classify;
    private String unit;
    private Integer distance;
}

package com.pwyl.modules.taking.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MyReleaseResponse implements Serializable {
    /**
     * 接单id
     */
    private Long id;
    /**
     * 发布状态
     */
    private String status;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 价格
     */
    private Double money;
    /**
     * 接单时间
     */
    private String orderTakingTime;
    private BigDecimal oldMoney;
    private BigDecimal memberMoney;
    private String gameImg;
    private String content;
    private String city;
    private Integer sec;
    private String createTime;
    private Integer classify;
    private String unit;
    private String myLevel;
    private String count;
    private String orderScore;
}

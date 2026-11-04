package com.pwyl.modules.taking.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwyl.modules.taking.entity.Game;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 接单返回字段
 */
@Data
public class TakingResponse implements Serializable {
    @ApiModelProperty("接单id")
    private Long id;
    @ApiModelProperty("游戏id类型")
    @TableField("game_id")
    private Long gameId;
    @TableField(exist = false)
    private String gameName;
    @ApiModelProperty("我的段位")
    @TableField("my_level")
    private String myLevel;
    @ApiModelProperty("接单段位")
    @TableField("order_level")
    private String orderLevel;
    @ApiModelProperty("接单时间")
    @TableField("order_taking_time")
    private String orderTakingTime;
    @ApiModelProperty("接单大区")
    @TableField("order_taking_area")
    private String orderTakingArea;
    @ApiModelProperty("价格")
    @TableField("money")
    private BigDecimal money;
    @ApiModelProperty("语音介绍")
    @TableField("voice_introduce")
    private String voiceIntroduce;
    @ApiModelProperty("主页图片")
    @TableField("homepage_img")
    private String homepageImg;
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;
    @ApiModelProperty("接单状态0进行中1待审核2已取消3完成")
    @TableField("status")
    private int status;
    @ApiModelProperty("是否是推荐接单0是1不是")
    @TableField("is_recommend")
    private String isRecommend;
    @ApiModelProperty("发布人")
    @TableField("user_id")
    private Long userId;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String avatar;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("人数")
    private int count;
    @ApiModelProperty("评分")
    @TableField("order_score")
    private Double orderScore;
    @ApiModelProperty("精度")
    private String longitude;
    @ApiModelProperty("维度")
    private String latitude;
    private BigDecimal oldMoney;
    private BigDecimal memberMoney;
    private String content;
    private String gameImg;
    private Integer sec;
    private Integer sex;
    private Integer age;
    private Integer classify;
    private String unit;
    private Integer distance;
//    private String createTime;
}

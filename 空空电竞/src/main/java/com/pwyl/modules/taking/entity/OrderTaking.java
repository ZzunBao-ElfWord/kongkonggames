package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwyl.modules.orders.entity.Orders;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderTaking implements Serializable {
    @ApiModelProperty("接单id")
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;
    @ApiModelProperty("游戏id类型")
    @TableField("game_id")
    private Long gameId;
    @TableField(exist = false)
    private Game game;
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
    @TableField("old_money")
    private BigDecimal oldMoney;
    @ApiModelProperty("价格")
    @TableField("money")
    private BigDecimal money;
    @ApiModelProperty("会员价格")
    @TableField("member_money")
    private BigDecimal memberMoney;
    @ApiModelProperty("语音介绍")
    @TableField("voice_introduce")
    private String voiceIntroduce;
    @ApiModelProperty("主页图片")
    @TableField("homepage_img")
    private String homepageImg;
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;
    @ApiModelProperty("接单状态0进行中1待审核2已取消")
    @TableField("status")
    private int status;
    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private String updateTime;
    @ApiModelProperty("是否是推荐接单0是1不是")
    @TableField("is_recommend")
    private String isRecommend;
    @ApiModelProperty("发布人")
    @TableField("user_id")
    private Long userId;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("人数")
    private Integer count;
    @ApiModelProperty("评分")
    @TableField("order_score")
    private Double orderScore;
    @ApiModelProperty("精度")
    private String longitude;
    @ApiModelProperty("维度")
    private String latitude;
    @TableField(exist = false)
    private List<Orders> orders = new ArrayList<>();
    @ApiModelProperty("假删除")
    private Long isdelete;
    private String content;
    private Integer sec;
    private Integer classify;
    private String unit;
    @ApiModelProperty("个人介绍")
    private String details;
    @ApiModelProperty("标签")
    private String label;
    @TableField(exist = false)
    private Integer rewardCount;
    @TableField(exist = false)
    private Integer myRewardCount;
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Integer sex;

    @TableField(exist = false)
    private Integer age;

    @TableField(exist = false)
    private String gameName;

    @TableField(exist = false)
    private String gameImg;

    @TableField(exist = false)
    private Integer distance;

    @TableField(exist = false)
    private BigDecimal score;

    @TableField(exist = false)
    private BigDecimal commentScore;

    @TableField(exist = false)
    private Integer commentCount;

    @TableField(exist = false)
    private Integer ordersCount;

    @TableField(exist = false)
    private BigDecimal userScore;

    @TableField(exist = false)
    private String fastContent;

    @TableField(exist = false)
    private String fastVoice;

    @TableField(exist = false)
    private String fastSec;
    @TableField(exist = false)
    private Long fastOrderTakeId;
    @TableField(exist = false)
    private Integer onLine;

    @TableField(exist = false)
    private String roomNo;

    @TableField(exist = false)
    private Integer isRoom;

    @TableField(exist = false)
    private Long roomId;
}

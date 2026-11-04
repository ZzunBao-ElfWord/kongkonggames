package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("游戏分类")
public class Game implements Serializable {
    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("游戏名称")
    @TableField("game_name")
    private String gameName;
    @ApiModelProperty("0启用1删除")
    @TableField("status")
    private Long status;
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;
    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private String updateTime;
    @ApiModelProperty("游戏图片")
    @TableField("game_img")
    private String  gameImg;
    private Integer gameType;
    private Integer sort;

}

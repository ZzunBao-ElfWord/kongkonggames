package com.pwyl.modules.taking.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TakingCommentResponse implements Serializable {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @TableField("user_name")
    private String userName;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 点赞量
     */
    private int count;
    /**
     * 等级
     */
    private String grade;

    private Integer score;

    private String createTime;


}

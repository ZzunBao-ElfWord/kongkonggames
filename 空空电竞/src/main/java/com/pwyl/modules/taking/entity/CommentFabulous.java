package com.pwyl.modules.taking.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description comment_fabulous
 * @author liyuan
 * @date 2021-08-12
 */
@Data
@ApiModel("comment_fabulous")
public class CommentFabulous implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 点赞id
     */
    @ApiModelProperty("点赞id")
    private Long id;

    /**
     * 接单评论id
     */
    @ApiModelProperty("接单评论id")
    private Long takingCommentId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    public CommentFabulous() {}
}
package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("接单评论")
public class TakingCommnt implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 接单id
     */
    private Long orderTakingId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 点赞次数
     */
    @TableField(exist = false)
    private Long count;
    /**
     * 邮件
     */
    private String mail;

    private Integer score;

    private Long ordersId;

}

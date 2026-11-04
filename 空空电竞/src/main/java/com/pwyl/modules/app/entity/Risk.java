package com.pwyl.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @description risk
 * @author fang
 * @date 2022-10-31
 */
@Data
@ApiModel("风险记录")
@TableName("risk")
public class Risk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 风险记录
     */
    private Long riskId;

    /**
     * 风险类型（1ip属地风险 2动态 3动态评论  4服务评论 5聊天 6举报）
     */
    private Integer riskType;

    /**
     * 用户id
     */
    private Long userId;

    @TableField(exist = false)
    private UserEntity user;


    /**
     * 触发内容
     */
    private String content;

    /**
     * 时间
     */
    private String createTime;

    public Risk() {}
}

package com.pwyl.modules.consortia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description consortia_apply
 * @author fang
 * @date 2022-06-30
 */
@Data
public class ConsortiaApply implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 公会申请id
     */
    @TableId(type = IdType.AUTO)
    private Long consortiaApplyId;

    /**
     * 公会id
     */
    private Long consortiaId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 1待审核 2同意 3拒绝
     */
    private Integer status;

    /**
     * 审批内容
     */
    private String content;

    /**
     * 分类 1申请 2退出
     */
    private Integer classify;

    /**
     * 创建时间
     */
    private String createTime;

    @TableField(exist = false)
    private String consortiaNo;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatar;

    public ConsortiaApply() {}
}

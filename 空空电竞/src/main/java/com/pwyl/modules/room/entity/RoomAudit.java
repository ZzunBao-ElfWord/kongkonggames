package com.pwyl.modules.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description room_audit
 * @author fang
 * @date 2023-12-27
 */
@Data
public class RoomAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 房间审核id
     */
    private Long roomAuditId;

    /**
     * 房间id
     */
    private String roomNo;

    private Long roomId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 房主id
     */
    private Long roomUserId;

    /**
     * 音频地址
     */
    private String url;

    private String urlTime;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * Block：建议屏蔽，Review ：建议人工复审，Pass：建议通过
     */
    private String suggestion;

    /**
     * Porn：色情，Abuse：谩骂，Ad：广告，Custom：自定义违规；以及其他令人反感、不安全或不适宜的内容类型。
     */
    private String labels;

    /**
     * 音频内容
     */
    private String audioText;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 状态 1待审核 2已处理 3已忽略
     */
    private Integer status;

    /**
     * 处罚结果 1封禁直播间 2封号 3警告
     */
    private Integer auditType;

    /**
     * 内容
     */
    private String auditContent;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String roomUserName;

    @TableField(exist = false)
    private String roomPhone;

    @TableField(exist = false)
    private String roomName;

    public RoomAudit() {}
}
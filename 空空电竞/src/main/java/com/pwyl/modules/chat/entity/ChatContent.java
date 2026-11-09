package com.pwyl.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 聊天内容对象 chat_content
 * 
 * @author fang
 * @date 2020-03-17
 */
@Data
@TableName("chat_content")
public class ChatContent {
    private static final long serialVersionUID = 1L;

    /** 聊天内容id */
    @TableId(type = IdType.INPUT)
    private Long chatContentId;

    /** 聊天会话id */
    private Long chatConversationId;

    /** 聊天内容 */
    private String content;

    /** 聊天类型 */
    private String messageType;

    /** 宽度 */
    private String width;

    /** 高度 */
    private String height;

    private String voiceLength;

    /** 发送人 */
    private Long userId;

    /** 状态（0未读 1已读） */
    private Integer status;
    /**
     * 创建时间
     */
    private String createTime;

}

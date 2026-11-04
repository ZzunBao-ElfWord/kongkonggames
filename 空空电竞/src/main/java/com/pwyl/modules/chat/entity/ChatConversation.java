package com.pwyl.modules.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 聊天会话对象 chat_conversation
 * 
 * @author fang
 * @date 2020-03-17
 */
@Data
@TableName("chat_conversation")
public class ChatConversation {
    private static final long serialVersionUID = 1L;

    /** 聊天会话id */
    @TableId(type = IdType.INPUT)
    private Long chatConversationId;

    /** 发起人id */
    private Long userId;

    /** 接收人id */
    private Long focusedUserId;

    /** 状态 */
    private Long status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /** 备注 */
    private String remark;

    private Integer isSendMsg;

    private Integer isWxMsg;

}

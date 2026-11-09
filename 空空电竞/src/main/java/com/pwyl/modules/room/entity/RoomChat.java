package com.pwyl.modules.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description room_chat
 * @author fang
 * @date 2023-12-01
 */
@Data
public class RoomChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 房间聊天记录id
     */
    private Long roomChatId;

    private Long roomId;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型类型 1文字 2图片 3语音 4表情包 5礼物 6订单
     */
    private String messageType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 语音时长
     */
    private String voiceLength;

    /**
     * 创建时间
     */
    private String createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String avatar;

    public RoomChat() {}
}

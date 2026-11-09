package com.pwyl.modules.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description room_user
 * @author fang
 * @date 2023-12-01
 */
@Data
public class RoomUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 房间人列表
     */
    private Long roomUserId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否上麦 1是
     */
    private Integer isSeat;

    /**
     * 上麦编号
     */
    private Integer seatNo;

    /**
     * 状态 1在线 2离开 3封禁
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 礼物数量
     */
    private Integer giftCount;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String roomName;

    @TableField(exist = false)
    private String roomNo;

    @TableField(exist = false)
    private Integer sex;


    public RoomUser() {}
}

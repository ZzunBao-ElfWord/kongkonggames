package com.pwyl.modules.room.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwyl.modules.app.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description room
 * @author fang
 * @date 2023-12-01
 */
@Data
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 房间编号
     */
    private String roomNo;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间类型 1陪玩 2点歌
     */
    private Integer roomType;

    /**
     * 房间图片
     */
    private String roomImg;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 房间公告
     */
    private String announcement;

    /**
     * 游戏玩法
     */
    private String playingMethod;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    @TableField(exist = false)
    private Integer roomUserCount;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String gameName;

    @TableField(exist = false)
    private Integer counts;

    @TableField(exist = false)
    private List<UserEntity> userEntityList;

    public Room() {}
}
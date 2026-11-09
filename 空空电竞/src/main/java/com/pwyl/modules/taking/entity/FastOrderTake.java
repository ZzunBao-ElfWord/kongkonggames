package com.pwyl.modules.taking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description fast_order_take
 * @author fang
 * @date 2022-04-21
 */
@Data
public class FastOrderTake implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 快速约玩接单记录id
     */
    @TableId(type = IdType.AUTO)
    private Long fastOrderTakeId;

    /**
     * 快速约玩id
     */
    private Long fastOrderId;

    /**
     * 接单用户id
     */
    private Long userId;

    /**
     * 接单文字
     */
    private String content;

    /**
     * 接单语音
     */
    private String voice;

    /**
     * 语音时长
     */
    private String sec;

    /**
     * 1待确认 2接单成功 3已取消
     */
    private Integer status;

    /**
     * 接单时间
     */
    private String createTime;

    public FastOrderTake() {}
}
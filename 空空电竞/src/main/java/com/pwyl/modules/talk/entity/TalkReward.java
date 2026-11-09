package com.pwyl.modules.talk.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description talk_reward
 * @author fang
 * @date 2021-09-01
 */
@Entity
@Data
public class TalkReward implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 打赏id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;

    /**
     * 用户id
     */
    @Column
    private Long userId;

    /**
     * 动态id
     */
    @Column
    private Long talkId;

    /**
     * 金额
     */
    @Column
    private BigDecimal money;

    /**
     * 时间
     */
    @Column
    private String createTime;

    public TalkReward() {}
}

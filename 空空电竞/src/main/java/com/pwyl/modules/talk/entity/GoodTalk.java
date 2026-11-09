package com.pwyl.modules.talk.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description good_talk
 * @author fang
 * @date 2021-08-16
 */
@Entity
@Data
public class GoodTalk implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 点赞id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodTalkId;

    /**
     * 动态id
     */
    @Column(name="talk_id")
    private Long talkId;

    /**
     * 用户id
     */
    @Column(name="user_id")
    private Long userId;

    /**
     * 时间
     */
    @Column(name="create_time")
    private String createTime;

    public GoodTalk() {
    }

}

package com.pwyl.modules.talk.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description comment
 * @author fang
 * @date 2021-08-16
 */
@Entity
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 评论id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    /**
     * 动态id
     */
    @Column()
    private Long talkId;

    /**
     * 用户id
     */
    @Column()
    private Long userId;

    /**
     * 内容id
     */
    @Column()
    private String content;

    /**
     *
     */
    @Column()
    private Integer parentId;

    /**
     * 被回复人
     */
    @Column
    private Long  byUserId;

    /**
     * 时间
     */
    @Column()
    private String createTime;

    public Comment() {
    }

}

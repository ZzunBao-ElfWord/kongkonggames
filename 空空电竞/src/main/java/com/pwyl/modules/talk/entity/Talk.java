package com.pwyl.modules.talk.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description talk
 * @author fang
 * @date 2021-08-16
 */
@Entity
@Data
public class Talk implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 动态id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long talkId;

    private Long gambitId;

    /**
     * 用户id
     */
    @Column
    private Long userId;

    /**
     * 内容
     */
    @Column
    private String content;

    /**
     * 图片
     */
    @Column
    private String img;

    @Column
    private String videoUrl;

    /**
     * 状态 1待审核 2已发布 3已下架
     */
    @Column
    private Integer status;

    @Column
    private Integer isDelete;

    /**
     * 创建时间
     */
    @Column
    private String createTime;

    /**
     * 分享次数
     */
    @Column
    private Integer shareNum;

    @Column
    private String auditContent;

    public Talk() {
    }

}

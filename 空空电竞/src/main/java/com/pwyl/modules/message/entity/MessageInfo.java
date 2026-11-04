package com.pwyl.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.taking.entity.OrderTaking;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/13
 */
@Data
@TableName("message_info")
public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 消息id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 图片
     */
    private String image;

    /**
     * is_see
     */
    private String isSee;

    /**
     * send_state
     */
    private String sendState;

    /**
     * send_time
     */
    private String sendTime;

    /**
     * 分类
     */
    private String state;

    /**
     * 标题
     */
    private String title;

    /**
     * 地址
     */
    private String url;

    /**
     * type
     */
    private String type;

    /**
     * platform  来源
     * 1 聊天页面新增投诉功按钮
     * 2 用户主页新增投诉按钮
     * 3 动态详情新增投诉按钮
     * 4 服务详情新增投诉按钮
     */
    private String platform;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 审核内容
     */
    private String auditContent;

    /**
     * 审核状态 1不做处理 2封号 3下架内容
     */
    private Integer status;

    /**
     * 被举报用户
     */
    private String byUserId;

    @TableField(exist = false)
    private String byUserName;

    /**
     * 来源id
     */
    private Long platformId;


    @TableField(exist = false)
    private Orders orders;

    @TableField(exist = false)
    private OrderTaking orderTaking;

    public MessageInfo() {}

}
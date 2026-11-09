package com.pwyl.modules.consortia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description consortia
 * @author fang
 * @date 2022-06-30
 */
@Data
public class Consortia implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 公会id
     */
    @TableId(type = IdType.AUTO)
    private Long consortiaId;

    /**
     * 公会名称
     */
    private String consortiaName;

    /**
     * 公会图片
     */
    private String consortiaImg;

    /**
     * 公会编号
     */
    private String consortiaNo;

    /**
     * 公会公告
     */
    private String consortiaContent;

    /**
     * 会长id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 手机号
     */
    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private BigDecimal sunMoney;

    @TableField(exist = false)
    private BigDecimal ordersMoney;

    @TableField(exist = false)
    private BigDecimal giftMoney;

    @TableField(exist = false)
    private Integer counts;


    public Consortia() {}
}
package com.pwyl.modules.app.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pwyl.modules.taking.entity.OrderTaking;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserFollowResponse implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 关注状态
     */
    private int status;
    /**
     * 时间
     */
    private String updateTime;

    private Integer sex;

    private Integer age;

    /**
     * 接单id
     */
    @TableField(exist = false)
    private Long  takingId;
    @TableField(exist = false)
    private OrderTaking orderTaking;
    @TableField(exist = false)
    private Integer onLine;
}

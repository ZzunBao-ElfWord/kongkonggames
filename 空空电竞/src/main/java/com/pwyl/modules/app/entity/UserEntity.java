package com.pwyl.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO,value = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;
	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private String updateTime;

	/**
	 * 苹果id
	 */
	@TableField("apple_id")
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	@TableField("sys_phone")
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	private Integer status;

	/**
	 * 来源 app 小程序 公众号
	 */
	private String platform;

	/**
	 * 积分
	 */
	private Integer jifen;

	/**
	 * 邀请码
	 */
	@TableField("invitation_code")
	private String invitationCode;

	/**
	 * 邀请人邀请码
	 */
	@TableField("inviter_code")
	private String inviterCode;

	/**
	 * app推送
	 */
	private String clientid;

	/**
	 * 支付宝账号
	 */
	private String zhiFuBao;

	/**
	 * 支付宝昵称
	 */
	private String zhiFuBaoName;

	/**
	 * 是否实名认证
	 */
	private Integer isAuthentication;

	/**
	 * 是否接受消息推送 1是 2否
	 */
	private Integer isSendMsg;

	/**
	 * 微信提现图片
	 */
	private String wxImg;

	/**
	 * 消息
	 */
	private String msg;

	/**
	 * 佣金比例
	 */
	private BigDecimal rate;

	/**
	 * 直属佣金比例
	 */
	private BigDecimal zhiRate;

	/**
	 * 非直属佣金比例
	 */
	private BigDecimal feiRate;

	/**
	 * 公会id
	 */
	private Long consortiaId;

	/**
	 * 公会订单抽成
	 */
	private BigDecimal consortiaOrdersRate;

	/**
	 * 公会礼物抽成
	 */
	private BigDecimal consortiaGiftRate;

	/**
	 * 用户ip
	 */
	private String userIp;

	/**
	 * ip归属地
	 */
	private String ipAddress;

	/**
	 * 是否是风险账号
	 */
	private Integer risk;

	/**
	 * 年月日 最近一次登录
	 */
	private String recentlyData;

	/**
	 * 1在线 2下线
	 */
	private Integer onLine;

	/**
	 * 儿童模式 1开启 2关闭
	 */
	private Integer babyModel;

	/**
	 * 儿童模式开启密码
	 */
	private String babyPwd;

	private Integer isRoom;

	private String roomNo;

	private Long roomId;

    /**
     * 是否是新用户 1是 2否
     */
    private Integer isNewUser;

	@TableField(exist = false)
	private String money;

	@TableField(exist = false)
	private Integer isVip;

	@TableField(exist = false)
	private Integer followCount;

	@TableField(exist = false)
	private String vipEndTime;

	@TableField(exist = false)
	private String consortiaName;

	@TableField(exist = false)
	private String consortiaNo;

	@TableField(exist = false)
	private Integer ordersCount;

}

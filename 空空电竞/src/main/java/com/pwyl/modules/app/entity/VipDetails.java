package com.pwyl.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import weixin.popular.bean.card.Discount;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("vip_details")
@ApiModel("会员详情")
public class VipDetails implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员类型")
    @TableField("vip_name_type")
    private Integer vipNameType;

    @ApiModelProperty("会员价格")
    private BigDecimal money;

    @TableField
    private String vipName;

    private BigDecimal award;
}

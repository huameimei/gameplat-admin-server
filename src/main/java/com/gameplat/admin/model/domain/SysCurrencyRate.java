package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @since 2021-11-27
 */
@Data
@TableName("sys_currency_rate")
public class SysCurrencyRate implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "货币id")
    @TableId(value = "currency_id", type = IdType.AUTO)
    private Integer currencyId;

    @ApiModelProperty(value = "货币名称")
    private String rechargeCurrencyName;

    @ApiModelProperty(value = "充值货币图标url")
    private String rechargeLoge;

    @ApiModelProperty(value = "充值ios货币比率")
    private BigDecimal rechargeIosRate;

    @ApiModelProperty(value = "充值android货币比率")
    private BigDecimal rechargeAndroidRate;

    @ApiModelProperty(value = "充值货币--积分货币比率")
    private BigDecimal rechargeIntegralRate;

    @ApiModelProperty(value = "积分兑换开关（0、关闭 1、开启）")
    private Integer rechargeIntegralSwitch;

    @ApiModelProperty(value = "积分货币名称")
    private String integralName;

    @ApiModelProperty(value = "积分货币图标url")
    private String integralLoge;

    @ApiModelProperty(value = "积分货币--充值货币比率")
    private BigDecimal integralRechargeRate;

    @ApiModelProperty(value = "充值货币兑换开关（0、关闭 1、开启）")
    private Integer integralRechargeSwitch;

    @ApiModelProperty(value = "提现货币名称")
    private String withdrawName;

    @ApiModelProperty(value = "提现货币图标url")
    private String withdrawLoge;

    @ApiModelProperty(value = "充值货币--提现货币比率")
    private BigDecimal rechargeWithdrawRate;

    @ApiModelProperty(value = "提现货币兑换开关（0、关闭 1、开启）")
    private Integer rechargeWithdrawSwitch;

    @ApiModelProperty(value = "提现货币--充值货币比率")
    private BigDecimal withdrawRechargeRate;

    @ApiModelProperty(value = "充值货币兑换开关（0、关闭 1、开启）")
    private Integer withdrawRechargeSwitch;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}

package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员活跃度VO对象
 * @author 沙漠
 * @since 2020-08-26
 */
@Data
public class MemberActivationVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String idName;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "真币加密串")
    private String goodMoney;

    @ApiModelProperty(value = "注册时间")
    private String createTime;

    @ApiModelProperty(value = "活跃时间")
    private String activationTime;

    @ApiModelProperty(value = "充值次数")
    private Integer rechargeCount;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal rechargeMoney;

    @ApiModelProperty(value = "充值优惠")
    private BigDecimal rechargeDiscounts;

    @ApiModelProperty(value = "其它优惠")
    private BigDecimal otherDiscounts;

    @ApiModelProperty(value = "提现次数")
    private Integer withdrawCount;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawMoney;

    @ApiModelProperty(value = "游戏输赢汇总")
    private BigDecimal gameWLSum;

    @ApiModelProperty(value = "游戏天数")
    private Integer gameDays;

    @ApiModelProperty(value = "公司收入汇总")
    private BigDecimal corporationIncomeSum;
}

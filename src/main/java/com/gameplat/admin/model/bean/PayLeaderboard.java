package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付排行榜数据结果
 *
 * @Author zak
 * @Date 2022/01/18 21:20:00
 */
@Data
public class PayLeaderboard {
    @ApiModelProperty("三方接口名称")
    private String interfaceName;

    @ApiModelProperty("三方接口名称")
    private String interfaceCode;

    @ApiModelProperty("使用平台数")
    private Integer interfaceUseNum;

    @ApiModelProperty("自动成功率")
    private BigDecimal autoSuccessRate;

    @ApiModelProperty("自动成功订单数")
    private Integer autoSuccessOrderNum;

    @ApiModelProperty("成功单数")
    private Integer successOrderNum;

    @ApiModelProperty("总单数")
    private Integer totalOrderNum;

    @ApiModelProperty("自动金额")
    private String autoSuccessAmount;

    @ApiModelProperty("总金额")
    private Integer totalAmount;
}

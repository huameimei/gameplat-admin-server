package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/3/5 21:42
 * @Version 1.0
 */
@Data
public class GameProxyDataVo implements Serializable {


    @ApiModelProperty(value = "代理总计")
    private BigDecimal allDivideAmount;


    @ApiModelProperty(value = "代理分红")
    private BigDecimal divideAmount;

    @ApiModelProperty(value = "代理分红人数")
    private int divideNum;

    @ApiModelProperty(value = "代理日工资")
    private BigDecimal salaryGrant;

    @ApiModelProperty(value = "代理日工资人数")
    private int salaryNum;

    @ApiModelProperty(value = "代理返点")
    private BigDecimal proxyWaterAmount;

    @ApiModelProperty(value = "代理返点人数")
    private int proxyWaterNum;


    public BigDecimal getAllDivideAmount() {
        return divideAmount.add(salaryGrant).add(proxyWaterAmount);
    }


    public GameProxyDataVo() {
        this.allDivideAmount = BigDecimal.ZERO;
        this.divideAmount = BigDecimal.ZERO;
        this.salaryGrant = BigDecimal.ZERO;
        this.proxyWaterAmount = BigDecimal.ZERO;
    }
}

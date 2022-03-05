package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/3/2 21:40
 * @Version 1.0
 */
@Data
public class GameWithDataReportVO implements Serializable {

    @ApiModelProperty(value = "出款总计金额")
    private BigDecimal allWithAmount;

    @ApiModelProperty(value = "出款总计人数")
    private int allWithCount;


    @ApiModelProperty(value = "会员提现金额")
    private BigDecimal withdrawMoney;

    @ApiModelProperty(value = "会员提现人数")
    private int withdrawCount;


    @ApiModelProperty(value = "人工提现金额")
    private BigDecimal handWithdrawMoney;

    @ApiModelProperty(value = "人工提现人数")
    private int handWithdrawCount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal counterFee;

    @ApiModelProperty(value = "首次提现金额")
    private BigDecimal firstWithMoney;

    @ApiModelProperty(value = "首次提现笔数")
    private int firstWithCount;

    @ApiModelProperty(value = "二次提现金额")
    private BigDecimal secondWithMoney;

    @ApiModelProperty(value = "二次提现笔数")
    private int secondWithCount;

    @ApiModelProperty(value = "三方提现金额")
    private BigDecimal thirdWithdrawMoney;

    @ApiModelProperty(value = "三方提现笔数")
    private int thirdWithdrawCount;



    @ApiModelProperty(value = "不计积分提现金额")
    private BigDecimal exceptionWithdrawAmount;

    @ApiModelProperty(value = "不计积分提现人数")
    private int exceptionWithdCount;

    @ApiModelProperty(value = "虚拟币提现金额")
    private BigDecimal virtualWithdrawMoney;

    @ApiModelProperty(value = "虚拟币提现人数")
    private int virtualWithdCount;


    public GameWithDataReportVO() {
        this.allWithAmount = BigDecimal.ZERO;
        this.allWithCount = 0;
        this.withdrawMoney = BigDecimal.ZERO;
        this.withdrawCount = 0;
        this.handWithdrawMoney = BigDecimal.ZERO;
        this.handWithdrawCount = 0;
        this.counterFee = BigDecimal.ZERO;
        this.firstWithMoney = BigDecimal.ZERO;
        this.firstWithCount = 0;
        this.secondWithMoney = BigDecimal.ZERO;
        this.secondWithCount = 0;
        this.thirdWithdrawMoney = BigDecimal.ZERO;
        this.thirdWithdrawCount = 0;
        this.exceptionWithdrawAmount = BigDecimal.ZERO;
        this.exceptionWithdCount = 0;
        this.virtualWithdrawMoney = BigDecimal.ZERO;
        this.virtualWithdCount = 0;
    }
}

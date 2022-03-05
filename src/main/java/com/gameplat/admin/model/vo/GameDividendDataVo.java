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
public class GameDividendDataVo implements Serializable {


    @ApiModelProperty(value = "优惠金额")
    private BigDecimal disAcountAmount;

    @ApiModelProperty(value = "彩金")
    private BigDecimal jackpot;

    @ApiModelProperty(value = "VIP红利")
    private BigDecimal vipDividend;

    @ApiModelProperty(value = "活动红利")
    private BigDecimal activityDividend;

    @ApiModelProperty(value = "聊天室红包")
    private BigDecimal redEnvelope ;

    @ApiModelProperty(value = "红利")
    private BigDecimal allDividend;

    public BigDecimal getAllDividend() {
        return disAcountAmount.add(jackpot).add(vipDividend).add(redEnvelope).add(allDividend);
    }


    public GameDividendDataVo() {
        this.disAcountAmount = BigDecimal.ZERO;
        this.jackpot = BigDecimal.ZERO;
        this.vipDividend = BigDecimal.ZERO;
        this.activityDividend = BigDecimal.ZERO;
        this.redEnvelope = BigDecimal.ZERO;
        this.allDividend = BigDecimal.ZERO;
    }
}

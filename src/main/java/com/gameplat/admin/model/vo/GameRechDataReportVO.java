package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameRechDataReportVO implements Serializable {

  @ApiModelProperty(value = "充值总计金额")
  private BigDecimal allRechAmount;

  @ApiModelProperty(value = "充值总计人数")
  private int allRechCount;

  @ApiModelProperty(value = "转账汇款金额")
  private BigDecimal bankMoney;

  @ApiModelProperty(value = "转账汇款人数")
  private int bankCount;

  @ApiModelProperty(value = "在线支付金额")
  private BigDecimal onlineMoney;

  @ApiModelProperty(value = "在线支付人数")
  private int onlineCount;

  @ApiModelProperty(value = "人工充值金额")
  private BigDecimal handMoney;

  @ApiModelProperty(value = "人工充值人数")
  private int handCount;

  @ApiModelProperty(value = "首次充值金额")
  private BigDecimal firstRechMoney;

  @ApiModelProperty(value = "首次充值笔数")
  private int firstRechCount;

  @ApiModelProperty(value = "二次充值金额")
  private BigDecimal secondRechMoney;

  @ApiModelProperty(value = "二次充值笔数")
  private int secondRechCount;

  @ApiModelProperty(value = "不计积分充值金额")
  private BigDecimal exceptionRechAmount;

  @ApiModelProperty(value = "不计积分充值人数")
  private int exceptionRechCount;

  @ApiModelProperty(value = "虚拟币充值金额")
  private BigDecimal virtualRechMoney;

  @ApiModelProperty(value = "虚拟币充值人数")
  private int virtualRechCount;

  public GameRechDataReportVO() {
    this.allRechAmount = BigDecimal.ZERO;
    this.allRechCount = 0;
    this.bankMoney = BigDecimal.ZERO;
    this.bankCount = 0;
    this.onlineMoney = BigDecimal.ZERO;
    this.onlineCount = 0;
    this.handMoney = BigDecimal.ZERO;
    this.handCount = 0;
    this.firstRechMoney = BigDecimal.ZERO;
    this.firstRechCount = 0;
    this.secondRechMoney = BigDecimal.ZERO;
    this.secondRechCount = 0;
    this.exceptionRechAmount = BigDecimal.ZERO;
    this.exceptionRechCount = 0;
    this.virtualRechMoney = BigDecimal.ZERO;
    this.virtualRechCount = 0;
  }
}

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameRechDataReportVO implements Serializable {

  @Schema(description = "充值总计金额")
  private BigDecimal allRechAmount;

  @Schema(description = "充值总计人数")
  private int allRechCount;

  @Schema(description = "转账汇款金额")
  private BigDecimal bankMoney;

  @Schema(description = "转账汇款人数")
  private int bankCount;

  @Schema(description = "在线支付金额")
  private BigDecimal onlineMoney;

  @Schema(description = "在线支付人数")
  private int onlineCount;

  @Schema(description = "人工充值金额")
  private BigDecimal handMoney;

  @Schema(description = "人工充值人数")
  private int handCount;

  @Schema(description = "首次充值金额")
  private BigDecimal firstRechMoney;

  @Schema(description = "首次充值笔数")
  private int firstRechCount;

  @Schema(description = "二次充值金额")
  private BigDecimal secondRechMoney;

  @Schema(description = "二次充值笔数")
  private int secondRechCount;

  @Schema(description = "不计积分充值金额")
  private BigDecimal exceptionRechAmount;

  @Schema(description = "不计积分充值人数")
  private int exceptionRechCount;

  @Schema(description = "虚拟币充值金额")
  private BigDecimal virtualRechMoney;

  @Schema(description = "虚拟币充值人数")
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

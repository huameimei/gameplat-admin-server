package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameWithDataReportVO implements Serializable {

  @Schema(description = "出款总计金额")
  private BigDecimal allWithAmount;

  @Schema(description = "出款总计人数")
  private int allWithCount;

  @Schema(description = "会员提现金额")
  private BigDecimal withdrawMoney;

  @Schema(description = "会员提现人数")
  private int withdrawCount;

  @Schema(description = "人工提现金额")
  private BigDecimal handWithdrawMoney;

  @Schema(description = "人工提现人数")
  private int handWithdrawCount;

  @Schema(description = "手续费")
  private BigDecimal counterFee;

  @Schema(description = "首次提现金额")
  private BigDecimal firstWithMoney;

  @Schema(description = "首次提现笔数")
  private int firstWithCount;

  @Schema(description = "二次提现金额")
  private BigDecimal secondWithMoney;

  @Schema(description = "二次提现笔数")
  private int secondWithCount;

  @Schema(description = "三方提现金额")
  private BigDecimal thirdWithdrawMoney;

  @Schema(description = "三方提现笔数")
  private int thirdWithdrawCount;

  @Schema(description = "不计积分提现金额")
  private BigDecimal exceptionWithdrawAmount;

  @Schema(description = "不计积分提现人数")
  private int exceptionWithdCount;

  @Schema(description = "虚拟币提现金额")
  private BigDecimal virtualWithdrawMoney;

  @Schema(description = "虚拟币提现人数")
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

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameDividendDataVo implements Serializable {

  @Schema(description = "优惠金额")
  private BigDecimal disAcountAmount;

  @Schema(description = "彩金")
  private BigDecimal jackpot;

  @Schema(description = "VIP红利")
  private BigDecimal vipDividend;

  @Schema(description = "活动红利")
  private BigDecimal activityDividend;

  @Schema(description = "聊天室红包")
  private BigDecimal redEnvelope;

  @Schema(description = "红利")
  private BigDecimal allDividend;

  public GameDividendDataVo() {
    this.disAcountAmount = BigDecimal.ZERO;
    this.jackpot = BigDecimal.ZERO;
    this.vipDividend = BigDecimal.ZERO;
    this.activityDividend = BigDecimal.ZERO;
    this.redEnvelope = BigDecimal.ZERO;
    this.allDividend = BigDecimal.ZERO;
  }

  public BigDecimal getAllDividend() {
    return disAcountAmount.add(jackpot).add(vipDividend).add(redEnvelope).add(allDividend);
  }
}

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameBetDataReportVO implements Serializable {

  @Schema(description = "游戏类型")
  private String gameType;

  @Schema(description = "投注金额")
  private BigDecimal betAmount;

  @Schema(description = "有效投注金额")
  private BigDecimal validAmount;

  @Schema(description = "输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "派彩金额")
  private BigDecimal payoutAmount;

  @Schema(description = "返水金额")
  private BigDecimal waterAmount;

  @Schema(description = "游戏人数")
  private int gameNum;

  public GameBetDataReportVO() {
    this.betAmount = BigDecimal.ZERO;
    this.validAmount = BigDecimal.ZERO;
    this.winAmount = BigDecimal.ZERO;
    this.payoutAmount = BigDecimal.ZERO;
    this.waterAmount = BigDecimal.ZERO;
    this.gameNum = 0;
  }

  public BigDecimal getWinAmount() {
    return this.winAmount.negate();
  }
}

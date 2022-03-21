package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameBetDataReportVO implements Serializable {

  @ApiModelProperty(value = "游戏类型")
  private String gameType;

  @ApiModelProperty(value = "投注金额")
  private BigDecimal betAmount;

  @ApiModelProperty(value = "有效投注金额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "派彩金额")
  private BigDecimal payoutAmount;

  @ApiModelProperty(value = "返水金额")
  private BigDecimal waterAmount;

  @ApiModelProperty(value = "游戏人数")
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

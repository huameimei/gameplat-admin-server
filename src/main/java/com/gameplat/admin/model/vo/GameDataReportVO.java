package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameDataReportVO implements Serializable {

  private Map<String, List<GameBetDataReportVO>> list = new HashMap<>(10);

  @ApiModelProperty(value = "输赢金额")
  private BigDecimal allWinAmount;

  @ApiModelProperty(value = "游戏人数")
  private int allWinNum;

  @ApiModelProperty(value = "返水总金额")
  private BigDecimal allWaterAmount;

  public GameDataReportVO() {
    this.allWinAmount = BigDecimal.ZERO;
    this.allWaterAmount = BigDecimal.ZERO;
  }

  // 以公司为维度取相反数
  public BigDecimal getAllWinAmount() {
    return this.allWinAmount.negate();
  }
}

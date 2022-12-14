package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "输赢金额")
  private BigDecimal allWinAmount;

  @Schema(description = "游戏人数")
  private int allWinNum;

  @Schema(description = "返水总金额")
  private BigDecimal allWaterAmount;

  public GameDataReportVO() {
    this.allWinAmount = BigDecimal.ZERO;
    this.allWaterAmount = BigDecimal.ZERO;
  }

}

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb @Date 2022/3/2 21:40 @Version 1.0
 */
@Data
public class GameWaterDataReportVO implements Serializable {

  @Schema(description = "游戏类型")
  private String gameType;

  @Schema(description = "返水金额")
  private BigDecimal waterAmount;

  public GameWaterDataReportVO() {
    this.waterAmount = BigDecimal.ZERO;
  }
}

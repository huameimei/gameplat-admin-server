package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/12 22:20
 */
@Data
public class GameDataVO implements Serializable {

  @Schema(description = "一级游戏编码")
  private String gameKind;

  @Schema(description = "有效投注额")
  private BigDecimal validAmount;

  @Schema(description = "输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "累计输赢金额")
  private BigDecimal accumulateWinAmount;
}

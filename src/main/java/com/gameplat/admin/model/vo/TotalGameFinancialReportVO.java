package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 游戏财务报表
 *
 * @author aBen
 * @date 2022/3/6 17:28
 */
@Data
public class TotalGameFinancialReportVO implements Serializable {

  @Schema(description = "有效投注额")
  private BigDecimal validAmount;

  @Schema(description = "输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "上月输赢金额")
  private BigDecimal lastWinAmount;

  @Schema(description = "累计输赢金额")
  private BigDecimal accumulateWinAmount;
}

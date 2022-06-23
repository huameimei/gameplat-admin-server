package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Author aguai @Description 游戏汇总报表 @Date 2022-01-26 */
@Data
public class GameWinVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计月份")
  private String countMonth;

  @Schema(description = "总计游戏盈利")
  private BigDecimal totalWinAmount;

  @Schema(description = "总计有效投注")
  private BigDecimal totalValidAmount;
}

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 平台费 @Author : cc @Date : 2022/3/23 */
@Data
public class PlatformFeeVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计月份")
  private String countMonth;

  @Schema(description = "游戏名称")
  private String gameName;

  @Schema(description = "游戏编码")
  private String gameCode;

  @Schema(description = "游戏盈利")
  private BigDecimal winAmount;

  @Schema(description = "有效投注")
  private BigDecimal validAmount;

  @Schema(description = "收费比例")
  private BigDecimal reportRate;

  @Schema(description = "游戏费用")
  private BigDecimal gameFee;
}

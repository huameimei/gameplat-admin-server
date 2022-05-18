package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivideSummaryDto {
  private Long id;

  @Schema(description = "分红期数表主键ID")
  private Long periodsId;

  @Schema(description = "分红代理的用户主键id")
  private Long userId;

  @Schema(description = "分红代理的名称")
  private String account;

  @Schema(description = "分红代理的层级")
  private Integer agentLevel;

  @Schema(description = "分红代理上级的用户id")
  private Long parentId;

  @Schema(description = "分红代理的上级")
  private String parentName;

  @Schema(description = "分红代理的代理路径")
  private String agentPath;

  @Schema(description = "汇总状态 1 已结算  2 已派发  3 部分派发(预留状态)")
  private Integer status;

  @Schema(description = "所有下级总的有效投注")
  private BigDecimal validAmount;

  @Schema(description = "所有下级总的输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "总分红金额")
  private BigDecimal divideAmount;

  @Schema(description = "真实总分红金额")
  private BigDecimal realDivideAmount;

  @Schema(description = "上期累计金额")
  private BigDecimal lastPeriodsAmount;
}

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
public class DivideDetailDto {
  private Long id;

  @Schema(description = "分红期数表主键ID")
  private Long periodsId;

  @Schema(description = "用户id")
  private Long userId;

  @Schema(description = "用户名")
  private String userName;

  @Schema(description = "用户类型")
  private String userType;

  @Schema(description = "分红代理的层级")
  private Integer agentLevel;

  @Schema(description = "分红代理的代理路径")
  private String superPath;

  @Schema(description = "分红代理的用户主键id")
  private Long proxyId;

  @Schema(description = "分红代理的名称")
  private String proxyName;

  private Long superId;

  private String superName;

  private Integer proxyAgentLevel;

  private String proxyAgentPath;

  @Schema(description = "游戏大类code")
  private String liveCode;

  @Schema(description = "一级游戏code")
  private String code;

  @Schema(description = "此用户的有效投注")
  private BigDecimal validAmount;

  @Schema(description = "此用户的输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "结算方式 1 输赢金额 2  有效投注")
  private Integer settleType;

  @Schema(description = "金额比例")
  private BigDecimal amountRatio;

  @Schema(description = "分红代理所在此条代理线的分红比例")
  private BigDecimal divideRatio;

  @Schema(description = "分红金额")
  private BigDecimal divideAmount;

  @Schema(description = "分红公式")
  private String divideFormula;
}

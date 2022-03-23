package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "分红期数表主键ID")
  private Long periodsId;

  @ApiModelProperty(value = "用户id")
  private Long userId;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "用户类型")
  private String userType;

  @ApiModelProperty(value = "分红代理的层级")
  private Integer agentLevel;

  @ApiModelProperty(value = "分红代理的代理路径")
  private String superPath;

  @ApiModelProperty(value = "分红代理的用户主键id")
  private Long proxyId;

  @ApiModelProperty(value = "分红代理的名称")
  private String proxyName;

  private Long superId;

  private String superName;

  private Integer proxyAgentLevel;

  private String proxyAgentPath;

  @ApiModelProperty(value = "游戏大类code")
  private String liveCode;

  @ApiModelProperty(value = "一级游戏code")
  private String code;

  @ApiModelProperty(value = "此用户的有效投注")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "此用户的输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "结算方式 1 输赢金额 2  有效投注")
  private Integer settleType;

  @ApiModelProperty(value = "金额比例")
  private BigDecimal amountRatio;

  @ApiModelProperty(value = "分红代理所在此条代理线的分红比例")
  private BigDecimal divideRatio;

  @ApiModelProperty(value = "分红金额")
  private BigDecimal divideAmount;

  @ApiModelProperty(value = "分红公式")
  private String divideFormula;
}

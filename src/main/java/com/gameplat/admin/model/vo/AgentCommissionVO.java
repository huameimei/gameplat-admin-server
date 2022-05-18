package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Author aguai @Description 下级代理佣金 @Date 2022-01-26 */
@Data
public class AgentCommissionVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计月份")
  private String countDate;

  @Schema(description = "代理层级")
  private Integer levelNum;

  @Schema(description = "上级账号")
  private String parentName;

  @Schema(description = "代理账号")
  private String agentName;

  @Schema(description = "下级代理数")
  private Integer subAgent;

  @Schema(description = "下级有效代理数")
  private Integer efficientAgent;

  @Schema(description = "下级会员佣金")
  private BigDecimal memberCommission;

  @Schema(description = "下级代理佣金")
  private BigDecimal agentCommission;

  @Schema(description = "佣金方案")
  private String planName;

  @Schema(description = "佣金等级")
  private String rebateLevel;
}

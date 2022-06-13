package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣报表 @Author : cc @Date : 2022/3/23 */
@Data
public class RebateReportDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "id")
  private Long reportId;

  @Schema(description = "统计月份")
  private String countDate;

  @Schema(description = "父级ID")
  private Long parentId;

  @Schema(description = "父级账号")
  private String parentName;

  @Schema(description = "代理ID")
  private Long agentId;

  @Schema(description = "代理账号")
  private String agentName;

  @Schema(description = "代理结构")
  private String agentPath;

  @Schema(description = "代理层级")
  private Integer levelNum;

  @Schema(description = "账号状态（1正常 0停用）")
  private Integer accountStatus;

  @Schema(description = "方案ID")
  private Long planId;

  @Schema(description = "至少实际佣金")
  private BigDecimal actualCommissionLeast;

  @Schema(description = "至多实际佣金")
  private BigDecimal actualCommissionMost;

  @Schema(description = "结算状态（0未结算 1风控审核 2财务审核 3已结算 4挂起）")
  private Integer status;
}

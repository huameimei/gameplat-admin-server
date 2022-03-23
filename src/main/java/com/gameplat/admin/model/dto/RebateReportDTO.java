package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣报表 @Author : cc @Date : 2022/3/23 */
@Data
public class RebateReportDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id")
  private Long reportId;

  @ApiModelProperty(value = "统计月份")
  private String countDate;

  @ApiModelProperty(value = "父级ID")
  private Long parentId;

  @ApiModelProperty(value = "父级账号")
  private String parentName;

  @ApiModelProperty(value = "代理ID")
  private Long agentId;

  @ApiModelProperty(value = "代理账号")
  private String agentName;

  @ApiModelProperty(value = "代理结构")
  private String agentPath;

  @ApiModelProperty(value = "代理层级")
  private Integer levelNum;

  @ApiModelProperty(value = "账号状态（1正常 0停用）")
  private Integer accountStatus;

  @ApiModelProperty(value = "方案ID")
  private Long planId;

  @ApiModelProperty(value = "至少实际佣金")
  private BigDecimal actualCommissionLeast;

  @ApiModelProperty(value = "至多实际佣金")
  private BigDecimal actualCommissionMost;

  @ApiModelProperty(value = "结算状态（0未结算 1风控审核 2财务审核 3已结算 4挂起）")
  private Integer status;
}

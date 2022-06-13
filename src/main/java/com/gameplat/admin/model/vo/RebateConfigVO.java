package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description : 返佣配置 @Author : cc @Date : 2022/3/22
 */
@Data
public class RebateConfigVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "id")
  private Long configId;

  @Schema(description = "方案ID")
  private Long planId;

  @Schema(description = "方案名称")
  private String planName;

  @Schema(description = "1 官网方案 0 默认方案")
  private Integer defaultFlag;

  @Schema(description = "下级佣金提成占比")
  private BigDecimal lowerCommission;

  @Schema(description = "下下级佣金提成占比")
  private BigDecimal subCommission;

  @Schema(description = "流水返利占比")
  private BigDecimal turnoverCommission;

  @Schema(description = "等级名称")
  private String rebateLevel;

  @Schema(description = "代理净盈利")
  private BigDecimal agentProfit;

  @Schema(description = "活跃会员数")
  private Integer activityMember;

  @Schema(description = "返佣比例")
  private BigDecimal commission;

  @Schema(description = "创建时间")
  private String createTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新时间")
  private String updateTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;
}

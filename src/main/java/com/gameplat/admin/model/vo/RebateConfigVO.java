package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣配置 @Author : cc @Date : 2022/3/22 */
@Data
public class RebateConfigVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id")
  private Long configId;

  @ApiModelProperty(value = "方案ID")
  private Long planId;

  @ApiModelProperty(value = "方案名称")
  private String planName;

  @ApiModelProperty(value = "1 官网方案 0 默认方案")
  private Integer defaultFlag;

  @ApiModelProperty(value = "下级佣金提成占比")
  private BigDecimal lowerCommission;

  @ApiModelProperty(value = "下下级佣金提成占比")
  private BigDecimal subCommission;

  @ApiModelProperty(value = "流水返利占比")
  private BigDecimal turnoverCommission;

  @ApiModelProperty(value = "等级名称")
  private String rebateLevel;

  @ApiModelProperty(value = "代理净盈利")
  private BigDecimal agentProfit;

  @ApiModelProperty(value = "活跃会员数")
  private Integer activityMember;

  @ApiModelProperty(value = "返佣比例")
  private BigDecimal commission;

  @ApiModelProperty(value = "创建时间")
  private String createTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新时间")
  private String updateTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "备注")
  private String remark;
}

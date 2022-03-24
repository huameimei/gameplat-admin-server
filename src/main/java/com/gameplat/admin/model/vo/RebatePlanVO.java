package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 返佣方案 @Author : cc @Date : 2022/3/22 */
@Data
public class RebatePlanVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id")
  private Long planId;

  @ApiModelProperty(value = "方案名称")
  private String planName;

  @ApiModelProperty(value = "下级佣金提成占比")
  private BigDecimal lowerCommission;

  @ApiModelProperty(value = "下下级佣金提成占比")
  private BigDecimal subCommission;

  @ApiModelProperty(value = "流水返利占比")
  private BigDecimal turnoverCommission;

  @ApiModelProperty(value = "1 官网方案 0 其他方案")
  private Integer defaultFlag;

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

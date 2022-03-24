package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Author aguai @Description 下级代理佣金 @Date 2022-01-26 */
@Data
public class AgentCommissionVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "统计月份")
  private String countDate;

  @ApiModelProperty(value = "代理层级")
  private Integer levelNum;

  @ApiModelProperty(value = "上级账号")
  private String parentName;

  @ApiModelProperty(value = "代理账号")
  private String agentName;

  @ApiModelProperty(value = "下级代理数")
  private Integer subAgent;

  @ApiModelProperty(value = "下级有效代理数")
  private Integer efficientAgent;

  @ApiModelProperty(value = "下级会员佣金")
  private BigDecimal memberCommission;

  @ApiModelProperty(value = "下级代理佣金")
  private BigDecimal agentCommission;

  @ApiModelProperty(value = "佣金方案")
  private String planName;

  @ApiModelProperty(value = "佣金等级")
  private String rebateLevel;
}

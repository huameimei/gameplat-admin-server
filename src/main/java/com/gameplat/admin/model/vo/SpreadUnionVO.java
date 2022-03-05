package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpreadUnionVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "联盟名称")
  private String unionName;

  @ApiModelProperty(value = "代理账号")
  private String agentAccount;

  @ApiModelProperty(value = "渠道类型")
  private String channel;

  @ApiModelProperty(value = "代理等级")
  private Integer agentLevel;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "联运收益")
  private BigDecimal income;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal rechargeAmount;

  @ApiModelProperty(value = "提现金额")
  private BigDecimal withdrawAmount;

  private Integer count;
}

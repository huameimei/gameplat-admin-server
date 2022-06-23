package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpreadUnionVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "联盟名称")
  private String unionName;

  @Schema(description = "代理账号")
  private String agentAccount;

  @Schema(description = "渠道类型")
  private String channel;

  @Schema(description = "代理等级")
  private Integer agentLevel;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "联运收益")
  private BigDecimal income;

  @Schema(description = "充值金额")
  private BigDecimal rechargeAmount;

  @Schema(description = "提现金额")
  private BigDecimal withdrawAmount;

  private Integer count;
}

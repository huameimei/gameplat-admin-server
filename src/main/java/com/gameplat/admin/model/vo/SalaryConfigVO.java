package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryConfigVO {
  private Long id;
  private Integer settleSort;
  private Integer agentLevel;
  private String gameType;
  private String gameTypeName;
  private BigDecimal winAmountLimit;
  private Integer validUserLimit;
  private BigDecimal validAmountLimit;
  private BigDecimal rechargeAmountLimit;
  private Integer isDirect;
  private Boolean isDirectBol;
  private Integer type;
  private BigDecimal amount;
  private BigDecimal amountLimit;
  private Date createTime;
  private String createBy;
  private String updateBy;
  private Date updateTime;
  private String remark;
}

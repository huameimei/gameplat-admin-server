package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeHistorySummaryVO {

  private Integer memberCount;

  private Integer rechargeCount;

  private BigDecimal amount;

  private BigDecimal discountAmount;

  private BigDecimal totalAmount;

  private String currencyCount;
}

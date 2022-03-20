package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryRechargeVO {
  private String account;
  private String superAccount;
  private String gameKind;
  private String gameType;
  private BigDecimal rechargeAmount;
  private BigDecimal validAmount;
  private BigDecimal winAmount;
}

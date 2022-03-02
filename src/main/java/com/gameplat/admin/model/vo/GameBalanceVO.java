package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameBalanceVO implements Serializable {
  private String platformCode;

  private BigDecimal balance;
}

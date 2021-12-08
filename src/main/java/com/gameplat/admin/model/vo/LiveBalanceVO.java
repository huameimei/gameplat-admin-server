package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class LiveBalanceVO implements Serializable {
  private String liveType;

  private BigDecimal balance;
}

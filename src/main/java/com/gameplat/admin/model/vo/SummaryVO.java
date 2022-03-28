package com.gameplat.admin.model.vo;

import java.math.BigDecimal;

public class SummaryVO {

  private BigDecimal allHandledSum;

  private BigDecimal allUnhandledSum;

  public BigDecimal getAllHandledSum() {
    if (null == allHandledSum) {
      return BigDecimal.ZERO;
    }
    return allHandledSum;
  }

  public void setAllHandledSum(BigDecimal allHandledSum) {
    this.allHandledSum = (null == allHandledSum ? BigDecimal.ZERO : allHandledSum);
  }

  public BigDecimal getAllUnhandledSum() {
    if (null == allUnhandledSum) {
      return BigDecimal.ZERO;
    }
    return allUnhandledSum;
  }

  public void setAllUnhandledSum(BigDecimal allUnhandledSum) {
    this.allUnhandledSum = (null == allUnhandledSum ? BigDecimal.ZERO : allUnhandledSum);
  }
}

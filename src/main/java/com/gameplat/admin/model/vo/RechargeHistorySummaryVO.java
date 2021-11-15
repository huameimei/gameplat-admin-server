package com.gameplat.admin.model.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class RechargeHistorySummaryVO {

  private Integer memberCount;

  private Integer rechargeCount;

  private BigDecimal amount;

  private BigDecimal discountAmount;

  private BigDecimal totalAmount;

  private String currencyCount;


//
//  public BigDecimal getAmount() {
//    if (null == amount) {
//      return BigDecimal.ZERO;
//    }
//    return amount;
//  }
//
//  public void setAmount(BigDecimal amount) {
//    this.amount = (null == amount ? BigDecimal.ZERO : amount);
//  }
//
//  public BigDecimal getDiscountAmount() {
//    if (null == discountAmount) {
//      return BigDecimal.ZERO;
//    }
//    return discountAmount;
//  }
//
//  public void setDiscountAmount(BigDecimal discountAmount) {
//    this.discountAmount = (null == discountAmount ? BigDecimal.ZERO : discountAmount);
//  }
//
//  public BigDecimal getTotalAmount() {
//    if (null == totalAmount) {
//      return BigDecimal.ZERO;
//    }
//    return totalAmount;
//  }
//
//  public void setTotalAmount(BigDecimal totalAmount) {
//    this.totalAmount = (null == totalAmount ? BigDecimal.ZERO : totalAmount);
//  }

}

package com.gameplat.admin.model.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MemberWithdrawHistorySummaryVO {

  private Integer memberCount;

  private Integer cashCount;

  private BigDecimal cashMoney;

  private BigDecimal approveMoney;

  private BigDecimal counterFee;

  private String approveCurrencyCount;


}

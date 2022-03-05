package com.gameplat.admin.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyPayBackResult {

  private Integer status; // 出款状态 0，出款中，1出款成功，2出款失败
  private String messge;
  private String responseMsg;
  private BigDecimal amount;
  private BigDecimal payAmount;
  private boolean success;
}

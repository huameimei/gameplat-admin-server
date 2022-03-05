package com.gameplat.admin.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RechargeBean implements Serializable {

  private static final long serialVersionUID = -3546957376584278721L;

  private Integer rechargeTimes;
  private BigDecimal rechargeAmount;
}

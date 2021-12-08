package com.gameplat.admin.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class RechargeBean implements Serializable {

  private static final long serialVersionUID = -3546957376584278721L;

  private Integer rechargeTimes;
  private BigDecimal rechargeAmount;

}

package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PpMerchantEditDTO implements Serializable {

  public Long id;

  private String name;

  private String ppInterfaceCode;

  private String parameters;

  private Integer sort;

  private String merLimits;

  private BigDecimal maxLimitCash; // 最大金额限制

  private BigDecimal minLimitCash; // 最小金额限制

  private String userLever; // 用户层级
}

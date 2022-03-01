package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PpMerchantAddDTO implements Serializable {

  private String name;

  private String ppInterfaceCode;

  private String parameters;

  private Integer sort;

  private String merLimits;

  private BigDecimal maxLimitCash; // 最大金额限制

  private BigDecimal minLimitCash; // 最小金额限制

  private String userLever; // 用户层级
}

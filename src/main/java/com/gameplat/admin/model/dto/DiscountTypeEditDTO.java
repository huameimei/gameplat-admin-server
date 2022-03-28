package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DiscountTypeEditDTO implements Serializable {

  public Long id;

  private String name;

  private Integer sort;

  private Integer mode;

  private BigDecimal discountAmount;
}

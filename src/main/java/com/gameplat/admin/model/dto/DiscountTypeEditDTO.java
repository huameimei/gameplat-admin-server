package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class DiscountTypeEditDTO implements Serializable {

  public Long id;

  private String name;

  private Integer sort;

  private Integer mode;

  private BigDecimal discountAmount;

}

package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class DiscountTypeEditDTO implements Serializable {

  public Long id;

  private String name;

  private Integer discountRatio;

  private Integer sort;

}

package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DiscountTypeEditDTO implements Serializable {

  public Long id;

  private String name;

  private Integer sort;

}

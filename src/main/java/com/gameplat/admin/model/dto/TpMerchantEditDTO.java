package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TpMerchantEditDTO implements Serializable {

  public Long id;

  private String name;

  private String parameters;

  private String payTypes;
}

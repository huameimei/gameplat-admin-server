package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class TpMerchantEditDTO implements Serializable {

  public Long id;

  private String name;

  private String parameters;

  private String payTypes;
}

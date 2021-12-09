package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class TpMerchantAddDTO implements Serializable {

  private String name;

  private String tpInterfaceCode;

  private String parameters;

  private String payTypes;
}

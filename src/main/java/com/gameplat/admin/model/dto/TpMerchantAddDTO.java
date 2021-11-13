package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TpMerchantAddDTO implements Serializable {

  private String name;

  private String tpInterfaceCode;

  private String parameters;

  private String payTypes;
}

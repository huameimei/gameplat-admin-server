package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TpInterfaceVO implements Serializable {

  private Long id;

  private String name;

  private String code;

  private String parameters;
}

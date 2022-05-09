package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PpInterfaceVO implements Serializable {

  private Long id;

  private String name;

  private String code;

  private String parameters;

  private Integer status;

  private String limitInfo;
}

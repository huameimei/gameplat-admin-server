package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperGamePlatformDTO implements Serializable {
  private Long id;

  private String code;

  private String name;

  private Integer transfer;

  private Integer sort;
}

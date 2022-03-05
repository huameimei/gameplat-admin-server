package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GamePlatformQueryDTO implements Serializable {
  private String platformCode;

  private Integer transfer;
}

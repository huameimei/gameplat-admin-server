package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameQueryDTO implements Serializable {
  private String platformCode;

  private String gameType;

  private String gameName;

  private Integer isH5;

  private Integer isPc;
}

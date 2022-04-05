package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameQueryDTO implements Serializable {
  private String platformCode;

  private String gameType;

  private String gameName;

  private String gameKind;

  private Integer isH5;

  private Integer isPc;
}

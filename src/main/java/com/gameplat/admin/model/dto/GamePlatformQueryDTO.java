package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GamePlatformQueryDTO implements Serializable {
  private String  platformCode;

  private Integer transfer;

}

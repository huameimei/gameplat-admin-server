package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperGamePlatformDTO implements Serializable {
  private Long id;

  private String code;

  private String name;

  private Integer transfer;
}

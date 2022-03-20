package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameTypeQueryDTO implements Serializable {

  private String gameTypeCode;

  private Integer status;
}

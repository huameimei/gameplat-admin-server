package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperGameDTO implements Serializable {

  private String gameCode;

  private String gameName;

  private String pcImgUrl;

  private String appImgUrl;

  private String platformCode;

  private String gameType;

  private Integer isH5;

  private Integer isFlash;

  private Integer sort;
}

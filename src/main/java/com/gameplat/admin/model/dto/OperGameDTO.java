package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperGameDTO implements Serializable {

  private String gameCode;

  private String chineseName;

  private String englishName;

  private String imageName;

  private String platformCode;

  private String gameType;

  private Integer isH5;

  private Integer isFlash;

  private String h5ImageName;

  private Integer sort;
}

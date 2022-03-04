package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameTypeQueryDTO implements Serializable {

  private String gameTypeCode;
  
  private Integer status;

}

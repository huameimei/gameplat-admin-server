package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperLiveBlacklistDTO implements Serializable {
  private Long id;

  private String target;

  private String blackType;

  private String liveCategory;

  private Integer targetType;

  private String remarks;
}

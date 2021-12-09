package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class BizBlacklistQueryDTO implements Serializable {
  private String userLevel;
  private String userAccount;
  private String blackType;
  private String liveCategory;
  private Integer status;
}

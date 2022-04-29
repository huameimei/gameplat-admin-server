package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BizBlacklistQueryDTO implements Serializable {

  private String userLevel;

  private String userAccount;

  private String blackType;

  private String liveCategory;

  private Integer status;
}

package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameRebateReportQueryDTO implements Serializable {

  private Long periodId;

  private String userAccount;

  private String beginDate;

  private String endDate;

  private Integer status;

  private String gameCode;

  private String gameKind;

  private int gameType;

  private String periodName;
}

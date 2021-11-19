package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class LiveRebateReportQueryDTO implements Serializable {

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

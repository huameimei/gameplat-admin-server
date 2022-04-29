package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class AgentReportQueryDTO {

  private String agentName;

  private String startDate;

  private String endDate;

  private Boolean isIncludeProxy;

  private String isAsc;

  private String orderByColumn;
}

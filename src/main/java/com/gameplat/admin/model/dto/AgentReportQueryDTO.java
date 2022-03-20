package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class AgentReportQueryDTO {
  public String agentName;
  public String startDate;
  public String endDate;
  public Boolean isIncludeProxy;
  public String isAsc;
  public String orderByColumn;
}

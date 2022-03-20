package com.gameplat.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryPeriodsDTO {
  private Long id;

  private String startDate;

  private String endDate;

  private Integer status;

  private String gameType;

  private String agentLevel;

  private Date createTime;

  private String createBy;

  private String updateBy;

  private Date updateTime;

  private String remark;
}

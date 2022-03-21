package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryPeriodsVO {
  private Long id;
  private String startDate;
  private String endDate;
  private Integer status;
  private String gameType;
  private String gameTypeName;
  private String[] gameCheckList;
  private String agentLevel;
  private String agentLevelLebel;
  private int[] agentCheckList;
  private Date createTime;
  private String createBy;
  private String updateBy;
  private Date updateTime;
  private String remark;
}

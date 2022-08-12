package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class KgNlWinReportQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String memberAccount;

  @Schema(description = "代理账号")
  private String proxyAccount;

  @Schema(description = "是否只查询直属下级")
  private Integer isDirectly;

  @Schema(description = "彩种类型 1官彩 2私彩 3六合彩")
  private Integer lottType;

  @Schema(description = "彩种玩法")
  private String gameCode;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;

  @Schema(description = "排序字段")
  private String orderByField;

  @Schema(description = "排序规则 asc升 desc降")
  private String orderBySort;
}

package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class LotteryRebateReportDTO {

  @Schema(description = "统计日期")
  private String countDate;

  @Schema(description = "享返点的代理名称")
  private String userName;

  @Schema(description = "投注人")
  private String betUserName;

  @Schema(description = "状态 0.未派发   1.已派发")
  private Integer status;

  @Schema(description = "搜索起始")
  private String beginTime;

  @Schema(description = "搜索结束")
  private String endTime;

  private List<String> userNameList;
}

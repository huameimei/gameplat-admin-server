package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LotteryRebateReportDTO {
  @ApiModelProperty(value = "统计日期")
  private String countDate;

  @ApiModelProperty(value = "享返点的代理名称")
  private String userName;

  @ApiModelProperty(value = "投注人")
  private String betUserName;

  @ApiModelProperty(value = "状态 0.未派发   1.已派发")
  private Integer status;

  @ApiModelProperty(value = "搜索起始")
  private String beginTime;

  @ApiModelProperty(value = "搜索结束")
  private String endTime;

  private List<String> userNameList;
}

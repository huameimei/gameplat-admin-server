package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动分发统计数据 @Author: lyq @Date: 2020/8/20 11:46 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistributeStatisticsVO implements Serializable {

  @Schema(description = "总计")
  private Double allMoney;

  @Schema(description = "小计")
  private Double subtotalMoney;
}

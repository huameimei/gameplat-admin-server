package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动分发统计数据 @Author: lyq @Date: 2020/8/20 11:46 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("活动分发统计数据")
public class ActivityDistributeStatisticsVO implements Serializable {

  @ApiModelProperty("总计")
  private Double allMoney;

  @ApiModelProperty("小计")
  private Double subtotalMoney;
}

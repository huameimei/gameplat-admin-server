package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动分发查询DTO @Author: kenvin @Date: 2020/8/20 11:45 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistributeQueryDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String username;

  @ApiModelProperty(value = "活动ID")
  private Long activityId;

  @ApiModelProperty(value = "状态（1 结算中，2 已结算）")
  private Integer status;

  @ApiModelProperty(value = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @ApiModelProperty(value = "资格审核开始时间")
  private String applyStartTime;

  @ApiModelProperty(value = "资格审核结束时间")
  private String applyEndTime;
}

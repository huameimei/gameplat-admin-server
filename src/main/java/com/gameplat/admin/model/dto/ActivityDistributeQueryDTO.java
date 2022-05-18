package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动分发查询DTO @Author: kenvin @Date: 2020/8/20 11:45 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistributeQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "活动ID")
  private Long activityId;

  @Schema(description = "状态（1 结算中，2 已结算）")
  private Integer status;

  @Schema(description = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @Schema(description = "资格审核开始时间")
  private String applyStartTime;

  @Schema(description = "资格审核结束时间")
  private String applyEndTime;
}

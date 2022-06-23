package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动资格查询DTO @Author: kenvin @Date: 2020/8/20 11:50 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "活动ID")
  private Long activityId;

  @Schema(description = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;

  @Schema(description = "审核状态（0 无效，1 申请中，2 已审核）")
  private Integer status;

  @Schema(description = "申请开始时间")
  private String applyStartTime;

  @Schema(description = "申请结束时间")
  private String applyEndTime;
}

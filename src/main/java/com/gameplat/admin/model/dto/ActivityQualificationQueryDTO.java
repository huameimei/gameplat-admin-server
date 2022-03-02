package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动资格查询DTO @Author: kenvin @Date: 2020/8/20 11:50 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationQueryDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String username;

  @ApiModelProperty(value = "活动ID")
  private Long activityId;

  @ApiModelProperty(value = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;

  @ApiModelProperty(value = "审核状态（0 无效，1 申请中，2 已审核）")
  private Integer status;

  @ApiModelProperty(value = "申请开始时间")
  private String applyStartTime;

  @ApiModelProperty(value = "申请结束时间")
  private String applyEndTime;
}

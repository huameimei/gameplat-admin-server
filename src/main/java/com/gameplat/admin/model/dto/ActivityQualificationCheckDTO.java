package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动资格检测DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationCheckDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @Schema(description = "用户账号")
  private String username;

  @Schema(description = "活动id")
  private Long activityId;

  @Schema(description = "统计日期")
  private String countDate;
}

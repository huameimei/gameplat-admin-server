package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动资格更新状态DTO @Author: lyq @Date: 2020/8/20 11:50 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationUpdateStatusDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @Schema(description = "资格id")
  private Long id;

  @Schema(description = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;
}

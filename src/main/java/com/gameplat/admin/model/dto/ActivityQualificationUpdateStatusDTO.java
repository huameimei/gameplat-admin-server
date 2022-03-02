package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/** 活动资格更新状态DTO @Author: lyq @Date: 2020/8/20 11:50 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("活动资格更新状态DTO")
public class ActivityQualificationUpdateStatusDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @ApiModelProperty(value = "资格id")
  private Long id;

  @ApiModelProperty(value = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;
}

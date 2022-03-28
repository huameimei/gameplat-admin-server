package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/** 活动资格审核DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("活动资格审核DTO")
public class ActivityQualificationAuditStatusDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @ApiModelProperty(value = "资格id数组")
  private List<Long> idList;

  @ApiModelProperty(value = "审核状态（0 无效，1 申请中，2 已审核）")
  private Integer status;
}

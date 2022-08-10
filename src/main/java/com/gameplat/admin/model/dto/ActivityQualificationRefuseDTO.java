package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/** 活动资格审核DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationRefuseDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @Schema(description = "资格id数组")
  private List<Long> idList;

  @Schema(description = "拒绝理由")
  private String refuseReason;
}

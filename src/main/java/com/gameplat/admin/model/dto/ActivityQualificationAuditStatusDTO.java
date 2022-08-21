package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** 活动资格审核DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationAuditStatusDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @Schema(description = "资格id数组")
  private List<Long> idList;

  @Schema(description = "审核状态（0 无效，1 申请中，2 已审核）")
  private Integer status;

  @Schema(description = "调整彩金金额")
  private BigDecimal adjustAmount;

  @Schema(description = "调整赠送金额")
  private BigDecimal adjustDml;
}

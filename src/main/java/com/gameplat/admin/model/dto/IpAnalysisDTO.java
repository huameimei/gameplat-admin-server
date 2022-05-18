package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ip分析
 *
 * @author lily
 */
@Data
public class IpAnalysisDTO {

  @Schema(description = "用户名")
  private String account;

  @Schema(description = "登录ip")
  private String loginIp;

  @Schema(description = "开始时间")
  private String beginTime;

  @Schema(description = "结束时间")
  private String endTime;

  @Schema(description = "用户id")
  private Integer memberId;

  @Schema(description = "类型")
  private Integer type;

  @Schema(description = "在线状态")
  private Integer online;
}

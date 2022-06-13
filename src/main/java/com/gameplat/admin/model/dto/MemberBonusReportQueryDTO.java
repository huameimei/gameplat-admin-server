package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberBonusReportQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "代理账号")
  private String superAccount;

  @Schema(description = "是否只查询直属下级 0否1是")
  private String flag;

  @Schema(description = "结束时间")
  private String startTime;

  @Schema(description = "开始时间")
  private String endTime;

  @Schema(description = "分页大小")
  private Integer pageSize;

  @Schema(description = "起始下标")
  private Integer from;
}

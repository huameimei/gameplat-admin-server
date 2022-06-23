package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class HgSportWinReportQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String memberAccount;

  @Schema(description = "代理账号")
  private String proxyAccount;

  @Schema(description = "是否只查询直属下级")
  private Integer isDirectly;

  @Schema(description = "盘口类型 1滚球 2今日 3早盘")
  private Integer handicapType;

  @Schema(description = "运动类型 1足球 2篮球")
  private Integer sportType;

  @Schema(description = "开始时间")
  @NotEmpty(message = "开始时间不能为空")
  private String beginTime;

  @Schema(description = "结束时间")
  @NotEmpty(message = "结束时间不能为空")
  private String endTime;
}

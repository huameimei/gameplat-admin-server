package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 会员日报表查询入参
 * @date 2022/1/5
 */
@Data
public class DayReportDTO implements Serializable {

  private static final long serialVersionUID = 7169928923640880406L;

  @Schema(description = "会员id")
  private Long memberId;

  @Schema(description = "会员账号(精确)")
  private String account;

  @Schema(description = "会员所属上级路径")
  private String superPath;

  @Schema(description = "开始时间")
  private String beginDate;

  @Schema(description = "结束时间")
  private String endDate;
}

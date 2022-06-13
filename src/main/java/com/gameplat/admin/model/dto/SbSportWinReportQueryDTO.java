package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/2/6 19:12
 * @desc
 */
@Data
public class SbSportWinReportQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String memberAccount;

  @Schema(description = "代理账号")
  private String proxyAccount;

  @Schema(description = "是否只查询直属下级")
  private Integer isDirectly;

  @Schema(description = "开始时间")
  @NotEmpty(message = "开始时间不能为空")
  private String beginTime;

  @Schema(description = "结束时间")
  @NotEmpty(message = "结束时间不能为空")
  private String endTime;
}

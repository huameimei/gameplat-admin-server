package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: asky
 * @date: 2021/4/10 13:51
 * @desc:
 */
@Data
public class IpStatisticsDTO {

  @Schema(description = "IP地址")
  private String loginIp;

  @Schema(description = "次数")
  private String frequency;

  @Schema(description = "分析类型")
  private Integer type;

  @Schema(description = "开始时间")
  private String beginTime;

  @Schema(description = "结束时间")
  private String endTime;
}

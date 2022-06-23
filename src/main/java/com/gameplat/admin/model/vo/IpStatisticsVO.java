package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IpStatisticsVO {

  @Schema(description = "ip地址")
  private String loginIp;

  @Schema(description = "次数")
  private String frequency;
}

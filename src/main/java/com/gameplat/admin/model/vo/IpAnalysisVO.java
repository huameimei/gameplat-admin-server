package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IpAnalysisVO {

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "ip地址")
  private String ipAddress;

  @Schema(description = "ip次数")
  private Integer ipCount;

  @Schema(description = "ip城市")
  private String loginAddress;

  @Schema(description = "在线状态 0不在线 1在线")
  private Integer offline;

  @Schema(description = "在线状态 0不在线 1在线")
  private String uuid;
}

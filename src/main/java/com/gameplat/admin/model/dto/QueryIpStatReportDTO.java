package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QueryIpStatReportDTO implements Serializable {

  @Schema(description = "IP地址")
  private String ip;

  @Schema(description = "分析类型")
  private Integer ipType;

  @Schema(description = "次数")
  private Integer count;

  @Schema(description = "开始时间")
  private Date beginDate;

  @Schema(description = "结束时间")
  private Date endDate;

  @Schema(description = "会员账号")
  private Date account;
}

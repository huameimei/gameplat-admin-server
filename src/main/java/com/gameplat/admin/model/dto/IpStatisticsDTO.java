package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: asky
 * @date: 2021/4/10 13:51
 * @desc:
 */
@Data
public class IpStatisticsDTO {

  @ApiModelProperty(value = "IP地址")
  private String loginIp;

  @ApiModelProperty(value = "次数")
  private String frequency;

  @ApiModelProperty(value = "分析类型")
  private Integer type;

  @ApiModelProperty(value = "开始时间")
  private String beginTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;
}

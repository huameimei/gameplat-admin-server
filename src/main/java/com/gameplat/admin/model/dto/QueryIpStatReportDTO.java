package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: lily
 * @Date: 2022/1/09
 */

@Data
public class QueryIpStatReportDTO implements Serializable {

  @ApiModelProperty(value = "IP地址")
  private String ip;
  @ApiModelProperty(value = "分析类型")
  private Integer ipType;
  @ApiModelProperty(value = "次数")
  private Integer count;
  @ApiModelProperty(value = "开始时间")
  private Date beginDate;
  @ApiModelProperty(value = "结束时间")
  private Date endDate;
  @ApiModelProperty(value = "会员账号")
  private Date account;
}

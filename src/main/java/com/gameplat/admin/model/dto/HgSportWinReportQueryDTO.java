package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/2/6 19:12
 * @desc
 */
@Data
public class HgSportWinReportQueryDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String memberAccount;

  @ApiModelProperty(value = "代理账号")
  private String proxyAccount;

  @ApiModelProperty(value = "是否只查询直属下级")
  private Integer isDirectly;

  @ApiModelProperty(value = "盘口类型 1滚球 2今日 3早盘")
  private Integer handicapType;

  @ApiModelProperty(value = "运动类型 1足球 2篮球")
  private String sportType;

  @ApiModelProperty(value = "开始时间")
  private String startTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;

}

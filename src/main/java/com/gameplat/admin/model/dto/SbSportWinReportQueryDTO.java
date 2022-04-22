package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "会员账号")
  private String memberAccount;

  @ApiModelProperty(value = "代理账号")
  private String proxyAccount;

  @ApiModelProperty(value = "是否只查询直属下级")
  private Integer isDirectly;

  @ApiModelProperty(value = "开始时间")
  @NotEmpty(message = "开始时间不能为空")
  private String beginTime;

  @ApiModelProperty(value = "结束时间")
  @NotEmpty(message = "结束时间不能为空")
  private String endTime;

}

package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/3/17 0:45
 * @desc
 */
@Data
public class MemberBonusReportQueryDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String userName;

  @ApiModelProperty(value = "代理账号")
  private String superAccount;

  @ApiModelProperty(value = "是否只查询直属下级 0否1是")
  private String flag;

  @ApiModelProperty(value = "结束时间")
  private String startTime;

  @ApiModelProperty(value = "开始时间")
  private String endTime;

  @ApiModelProperty(value = "分页大小")
  private Integer pageSize;

  @ApiModelProperty(value = "起始下标")
  private Integer from;

}

package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description VIP会员签到汇总入参
 * @date 2021/11/24
 */
@Data
public class MemberVipSignStatisDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("会员账号")
  private String userName;

  @ApiModelProperty(value = "最后签到开始时间")
  private String beginTime;

  @ApiModelProperty(value = "最后签到结束时间")
  private String endTime;
}

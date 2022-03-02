package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description : VIP福利记录入参 @Author : lily @Date : 2021/11/23
 */
@Data
public class MemberWealRewordDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("用户ID")
  private Long userId;

  @ApiModelProperty("用户账号")
  private String userName;

  @ApiModelProperty("状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  private Integer status;

  @ApiModelProperty(value = "流水号")
  private String serialNumber;

  @ApiModelProperty("类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  private Integer type;

  @ApiModelProperty("开始时间")
  private String startTime;

  @ApiModelProperty("结束时间")
  private String endTime;

  @ApiModelProperty("vip等级")
  private Integer vipLevel;
}

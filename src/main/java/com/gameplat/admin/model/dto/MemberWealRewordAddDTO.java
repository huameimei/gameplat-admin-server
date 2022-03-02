package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 新增福利记录入参
 * @date 2021/11/28
 */
@Data
public class MemberWealRewordAddDTO implements Serializable {

  @ApiModelProperty("用户ID")
  private Long userId;

  @ApiModelProperty("用户账号")
  private String userName;

  @ApiModelProperty("状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  private Integer status;

  @ApiModelProperty("老等级")
  private Integer oldLevel;

  @ApiModelProperty("当前等级")
  private Integer currentLevel;

  @ApiModelProperty("派发金额")
  private BigDecimal rewordAmount;

  @ApiModelProperty("提现打码量")
  private BigDecimal withdrawDml;

  @ApiModelProperty("类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  private Integer type;

  @ApiModelProperty(value = "领取时间")
  private Date drawTime;

  @ApiModelProperty(value = "失效时间")
  private Date invalidTime;

  @ApiModelProperty(value = "流水号")
  private String serialNumber;

  @ApiModelProperty(value = "活动标题")
  private String activityTitle;

  @ApiModelProperty(value = "备注")
  private String remark;
}

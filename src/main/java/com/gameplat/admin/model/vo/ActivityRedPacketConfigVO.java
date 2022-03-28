package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动红包配置
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "活动红包配置VO", description = "活动红包配置")
public class ActivityRedPacketConfigVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty("每期红包总额上限")
  private Integer redenvelopeMoneyMax;

  @ApiModelProperty("每期红包总额下限")
  private Integer redenvelopeMoneyMim;

  @ApiModelProperty("活动黑名单")
  private String activityBalcklist;

  @ApiModelProperty("选择红包雨活动ID")
  private Long redenvelopeId;

  @ApiModelProperty("红包雨活动开始时间")
  private String redenvelopeBeginTime;

  @ApiModelProperty("红包雨活动结束时间")
  private String redenvelopeEndTime;

  @ApiModelProperty("转盘抽奖活动开始时间")
  private String rotarySwitchBeginTime;

  @ApiModelProperty("转盘抽奖活动结束时间")
  private String rotarySwitchEndTime;

  @ApiModelProperty("选择转盘抽奖活动ID")
  private Long rotarySwitchId;

  @ApiModelProperty("周末红包活动日期")
  private String weekendRedenvelopeDay;

  @ApiModelProperty("周末红包活动ID")
  private Long weekendRedenvelopeId;

  @ApiModelProperty("是否启用ip限制")
  private Boolean redenvelopeIsIpLimit;

  @ApiModelProperty("聊天室红包配置")
  private String redenvelopeChat;

  @ApiModelProperty("打码量倍数")
  private Integer dmlMultiple;

  @ApiModelProperty("是否自动派发红包")
  private Boolean redenvelopeIsAutoDistribute;
}

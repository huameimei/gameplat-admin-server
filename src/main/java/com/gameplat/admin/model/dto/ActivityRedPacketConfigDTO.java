package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 红包活动配置
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketConfigDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "每期红包总额上限")
  private Integer redenvelopeMoneyMax;

  @Schema(description = "每期红包总额下限")
  private Integer redenvelopeMoneyMim;

  @Schema(description = "活动黑名单")
  private String activityBalcklist;

  @Schema(description = "选择红包雨活动ID")
  private Long redenvelopeId;

  @Schema(description = "红包雨活动开始时间")
  private String redenvelopeBeginTime;

  @Schema(description = "红包雨活动结束时间")
  private String redenvelopeEndTime;

  @Schema(description = "转盘抽奖活动开始时间")
  private String rotarySwitchBeginTime;

  @Schema(description = "转盘抽奖活动结束时间")
  private String rotarySwitchEndTime;

  @Schema(description = "选择转盘抽奖活动ID")
  private Long rotarySwitchId;

  @Schema(description = "周末红包活动日期")
  private String weekendRedenvelopeDay;

  @Schema(description = "周末红包活动ID")
  private Long weekendRedenvelopeId;

  @Schema(description = "是否启用ip限制")
  private String redenvelopeIsIpLimit;

  @Schema(description = "聊天室红包配置")
  private String redenvelopeChat;

  @Schema(description = "打码量倍数")
  private Integer dmlMultiple;

  @Schema(description = "是否自动派发红包")
  private Boolean redenvelopeIsAutoDistribute;

  @Schema(description = "红包雨默认图片")
  private String pic;
}

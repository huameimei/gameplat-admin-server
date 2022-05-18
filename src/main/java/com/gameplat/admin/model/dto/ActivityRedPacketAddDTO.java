package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 红包雨
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "红包时间(周一到周日)")
  private String weekTime;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "红包雨标题")
  private String realTitle;

  @Schema(description = "红包雨位置")
  private String realLocation;

  @Schema(description = "频率")
  private String frequency;

  @Schema(description = "持续时长、下雨时长")
  private String duration;

  @Schema(description = "红包类型（1红包雨，2运营红包）")
  private Integer packetType;

  @Schema(description = "红包总个数")
  private Integer packetTotalNum;

  @Schema(description = "用户抽红包总次数限制")
  private Integer packetDrawLimit;

  @Schema(description = "启动时间(时分秒)")
  private String startTime;

  @Schema(description = "终止时间(时分秒)")
  private String stopTime;

  @Schema(description = "红包条件")
  private List<ActivityRedPacketConditionDTO> redPacketConditionList;

  @Schema(description = "奖品列表")
  private List<ActivityPrizeDTO> activityPrize;
}

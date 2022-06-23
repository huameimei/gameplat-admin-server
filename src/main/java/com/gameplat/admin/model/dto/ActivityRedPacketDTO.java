package com.gameplat.admin.model.dto;

import com.gameplat.base.common.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 活动红包雨
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityRedPacketDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "页面大小")
  private Integer pageSize;

  @Schema(description = "第几页")
  private Integer pageNum;

  @Schema(description = "主键")
  private Long[] ids;

  @Schema(description = "主键")
  @Mapping(value = "packetId")
  private Long packetId;

  @Schema(description = "红包时间(周一到周日)")
  private String weekTime;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

  @Schema(description = "红包雨标题")
  private String realTitle;

  @Schema(description = "红包雨位置")
  private String realLocation;

  @Schema(description = "频率")
  private String frequency;

  @Schema(description = "持续时长")
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

  @Schema(description = "被删除的红包条件列表")
  private Long[] deleteConditionId;

  @Schema(description = "被删除的奖品列表")
  private Long[] deleteIdList;

  @Schema(description = "红包条件")
  private List<ActivityRedPacketConditionDTO> redPacketCondition;

  @Schema(description = "奖品列表")
  private List<ActivityPrizeDTO> activityPrize;

  public void setBeginTime(String beginTime) {
    this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  public void setEndTime(String endTime) {
    this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}

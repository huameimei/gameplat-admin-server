package com.gameplat.admin.model.dto;

import com.gameplat.base.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MemberRedPacket对象", description = "红包雨")
public class ActivityRedPacketDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "页面大小")
  private Integer pageSize;

  @ApiModelProperty(value = "第几页")
  private Integer pageNum;

  @ApiModelProperty(value = "主键")
  private Long[] ids;

  @ApiModelProperty(value = "主键")
  @Mapping(value = "packetId")
  private Long packetId;

  @ApiModelProperty(value = "红包时间(周一到周日)")
  private String weekTime;

  @ApiModelProperty(value = "开始时间")
  private Date beginTime;

  @ApiModelProperty(value = "结束时间")
  private Date endTime;

  @ApiModelProperty(value = "状态 0下线 1上线")
  private Integer status;

  @ApiModelProperty(value = "红包雨标题")
  private String realTitle;

  @ApiModelProperty(value = "红包雨位置")
  private String realLocation;

  @ApiModelProperty(value = "频率")
  private String frequency;

  @ApiModelProperty(value = "持续时长")
  private String duration;

  @ApiModelProperty(value = "红包类型（1红包雨，2运营红包）")
  private Integer packetType;

  @ApiModelProperty(value = "红包总个数")
  private Integer packetTotalNum;

  @ApiModelProperty(value = "用户抽红包总次数限制")
  private Integer packetDrawLimit;

  @ApiModelProperty(value = "启动时间(时分秒)")
  private String startTime;

  @ApiModelProperty(value = "终止时间(时分秒)")
  private String stopTime;

  @ApiModelProperty(value = "被删除的红包条件列表")
  private Long[] deleteConditionId;

  @ApiModelProperty(value = "被删除的奖品列表")
  private Long[] deleteIdList;

  @ApiModelProperty(value = "红包条件")
  private List<ActivityRedPacketConditionDTO> redPacketCondition;

  @ApiModelProperty(value = "奖品列表")
  private List<ActivityPrizeDTO> activityPrize;

  public void setBeginTime(String beginTime) {
    this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  public void setEndTime(String endTime) {
    this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}

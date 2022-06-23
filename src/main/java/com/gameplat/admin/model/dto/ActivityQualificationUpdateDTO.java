package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/** 活动资格更新DTO @Author: kenvin @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityQualificationUpdateDTO implements Serializable {

  private static final long serialVersionUID = -3594282509149807621L;

  @Schema(description = "资格id")
  private Long qualificationId;

  @Schema(description = "资格id")
  private Long id;

  /** 1 活动大厅 2 红包雨 */
  @Schema(description = "活动类型，1 活动大厅")
  private Integer type;

  @Schema(description = "活动名称")
  private String activityName;

  @Schema(description = "活动类型（1 活动大厅，2 红包雨，3 转盘）")
  private Integer activityType;

  @Schema(description = "活动ID")
  private Long activityId;

  @Schema(description = "用户id")
  private Long userId;

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "申请时间")
  private Date applyTime;

  @Schema(description = "申请开始时间")
  private String applyStartTime;

  @Schema(description = "申请结束时间")
  private String applyEndTime;

  @Schema(description = "审核人")
  private String auditPerson;

  @Schema(description = "审核时间")
  private Date auditTime;

  @Schema(description = "审核备注")
  private String auditRemark;

  @Schema(description = "状态（0 无效，1 申请中，2 已审核）")
  private Integer status;

  @Schema(description = "活动开始时间")
  private Date activityStartTime;

  @Schema(description = "活动结束时间")
  private Date activityEndTime;

  @Schema(description = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;

  @Schema(description = "截止时间")
  private Date abortTime;

  @Schema(description = "总抽奖次数")
  private Integer drawNum;

  @Schema(description = "使用次数")
  private Integer employNum;

  @Schema(description = "最小金额")
  private Integer minMoney;

  @Schema(description = "最大金额")
  private Integer maxMoney;

  @Schema(description = "删除（0 已删除，1 未删除）")
  private Integer deleteFlag;

  @Schema(description = "使用时间")
  private Date employTime;

  @Schema(description = "统计项目")
  private Integer statisItem;

  @Schema(description = "提现打码量")
  private Integer withdrawDml;

  @Schema(description = "奖励详情")
  private String awardDetail;

  @Schema(description = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @Schema(description = "页面大小")
  private Integer pageSize;

  @Schema(description = "第几页")
  private Integer pageNum;

  @Schema(description = "统计开始时间")
  private Date statisStartTime;

  @Schema(description = "统计结束时间")
  private Date statisEndTime;
}

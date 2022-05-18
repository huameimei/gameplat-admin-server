package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/** 活动资格VO @Author: whh @Date: 2020/8/25 15:48 @Description: 资格检测 VO */
@Data
public class ActivityQualificationVO {

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "主键ID")
  private Long id;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "用户id")
  private Long userId;

  @Schema(description = "活动名称")
  private String activityName;

  @Schema(description = "活动类型（1 活动大厅，2 红包雨，3 转盘）")
  private Integer activityType;

  @Schema(description = "活动ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long activityId;

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "申请时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date applyTime;

  @Schema(description = "审核人")
  private String auditPerson;

  @Schema(description = "审核备注")
  private String auditRemark;

  @Schema(description = "状态（0 无效，1 申请中，2 已审核）")
  private Integer status;

  @Schema(description = "资格状态（0 禁用，1 启用）")
  private Integer qualificationStatus;

  @Schema(description = "用户状态 0:非正常 1:正常")
  private Integer userStatus;

  @Schema(description = "用户充值层级")
  private String rank;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）")
  private Integer statisItem;

  @Schema(description = "提现打码量")
  private Integer withdrawDml;

  @Schema(description = "奖励详情")
  private String awardDetail;

  @Schema(description = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @Schema(description = "统计开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date statisStartTime;

  @Schema(description = "统计结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date statisEndTime;

  @Schema(description = "审核时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date auditTime;

  @Schema(description = "活动开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date activityStartTime;

  @Schema(description = "活动结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date activityEndTime;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "截止时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date abortTime;

  @Schema(description = "使用次数")
  private Integer employNum;

  @Schema(description = "最小金额")
  private Integer minMoney;

  @Schema(description = "最大金额")
  private Integer maxMoney;

  @Schema(description = "删除（0 已删除，1 未删除）")
  private Integer deleteFlag;

  @Schema(description = "使用时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date employTime;

  @Schema(description = "与活动派发关联id")
  private String qualificationActivityId;

  @Schema(description = "唯一标识")
  private String soleIdentifier;
}

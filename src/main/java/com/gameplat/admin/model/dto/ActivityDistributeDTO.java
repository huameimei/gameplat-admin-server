package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** 活动派发DTO @Author: lyq @Date: 2020/8/20 11:45 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("activity_distribute")
public class ActivityDistributeDTO implements Serializable {

  private static final long serialVersionUID = 4142676336119312814L;

  @Schema(description = "派发id集合")
  private List<Long> distributeIds;

  @Schema(description = "派发id")
  private Long distributeId;

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

  @Schema(description = "资格审核时间")
  private Date applyTime;

  @Schema(description = "资格审核开始时间")
  private String applyStartTime;

  @Schema(description = "资格审核结束时间")
  private String applyEndTime;

  @Schema(description = "优惠金额")
  private Integer discountsMoney;

  @Schema(description = "优惠奖品")
  private String discountsPrize;

  @Schema(description = "结算时间")
  private Date settlementTime;

  @Schema(description = "状态（1 结算中，2 已结算）")
  private Integer status;

  @Schema(description = "截止时间")
  private Date abortTime;

  @Schema(description = "删除（0 已删除，1 未删除）")
  private Integer deleteFlag;

  @Schema(description = "会员充值层级")
  private Integer memberPayLevel;

  @Schema(description = "状态（1 结算中，2 已结算, 3過期） ")
  private List<String> statusList;

  @Schema(description = "是否失效（0 失效，1 未失效）")
  private Integer disabled;

  @Schema(description = "过期时间")
  private String expired;

  @Schema(description = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @Schema(description = "提现打码量")
  private Integer withdrawDml;

  @Schema(description = "统计开始时间")
  private Date statisStartTime;

  @Schema(description = "统计结束时间")
  private Date statisEndTime;
}

package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 活动分发 @Author: lyq @Date: 2020/8/20 11:46 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityDistributeVO implements Serializable {

  private static final long serialVersionUID = -3023519898125789485L;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "派发id")
  private Long distributeId;

  @Schema(description = "活动名称")
  private String activityName;

  @Schema(description = "活动类型（1 活动大厅，2 红包雨，3 转盘）")
  private Integer activityType;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "活动ID")
  private Long activityId;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "用户id")
  private Long userId;

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "申请时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date applyTime;

  @Schema(description = "优惠金额")
  private BigDecimal discountsMoney;

  @Schema(description = "优惠奖品")
  private String discountsPrize;

  @Schema(description = "结算时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date settlementTime;

  @Schema(description = "状态（1 结算中，2 已结算） ")
  private Integer status;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "是否失效（0 失效，1 未失效）")
  private Integer disabled;

  @Schema(description = "活动多重彩金是否开启  多重彩金（0 否，1 是）")
  private Integer multipleHandsel;

  @Schema(description = "删除（0 已删除，1 未删除）")
  private Integer deleteFlag;

  @Schema(description = "会员充值层级")
  private Integer memberPayLevel;

  @Schema(description = "与资格管理关联id")
  private String qualificationActivityId;

  @Schema(description = "唯一标识")
  private String soleIdentifier;

  @Schema(description = "统计开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date statisStartTime;

  @Schema(description = "统计结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date statisEndTime;

  @Schema(description = "领取方式（1 直接发放，2 福利中心）")
  private Integer getWay;

  @Schema(description = "提现打码量")
  private Integer withdrawDml;
}

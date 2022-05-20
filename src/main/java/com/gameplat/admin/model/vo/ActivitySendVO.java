package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author: whh @Date: 2020/8/21 16:53 @Description: 定时查询 订单表参数
 */
@Data
public class ActivitySendVO {

  @Schema(description = "活动Id")
  private String activityId;

  @Schema(description = "开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date startDate;

  @Schema(description = "结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date endDate;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "用户Id")
  private Long userId;

  @Schema(description = "用户名")
  private String userName;

  @Schema(description = "首充金额")
  private String firstAmount;

  @Schema(description = "用户充值金额")
  private String amount;

  @Schema(description = "用户充值金额(用于计算)")
  private Double cumulativeAmount;

  @Schema(description = "充值时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date payTime;
}

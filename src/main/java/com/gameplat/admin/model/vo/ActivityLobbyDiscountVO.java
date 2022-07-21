package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/** 活动大厅优惠VO @Author: lyq @Date: 2020/8/20 15:19 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLobbyDiscountVO implements Serializable {

  private static final long serialVersionUID = -3883439363075850398L;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "大厅优惠id")
  private Long lobbyDiscountId;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "活动大厅id")
  private Long lobbyId;

  @Schema(description = "优惠url")
  private String discountUrl;

  @Schema(description = "目标值")
  private Long targetValue;

  @Schema(description = "赠送值")
  private BigDecimal presenterValue;

  @Schema(description = "赠送打码")
  private BigDecimal presenterDml;

  @Schema(description = "提现打码")
  private BigDecimal withdrawDml;
}

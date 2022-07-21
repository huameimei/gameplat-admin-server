package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/** 活动大厅打折信息 @Author: lyq @Date: 2020/8/20 14:28 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLobbyDiscountDTO implements Serializable {

  private static final long serialVersionUID = 2027127567683724890L;

  @Schema(description = "大厅优惠id")
  private Long lobbyDiscountId;

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

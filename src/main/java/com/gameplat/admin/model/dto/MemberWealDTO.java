package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @description 查询福利入参
 * @date 2021/11/22
 */
@Data
public class MemberWealDTO implements Serializable {

  @Schema(description = "福利名称")
  private String name;

  @Schema(description = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
  private Integer type;

  @Schema(description = "状态 0:未结算  1：未派发   2：已派发  3：已回收")
  private Integer status;

  @Schema(description = "周期  开始时间")
  private String startDate;

  @Schema(description = "周期  结束时间")
  private String endDate;

  @Schema(description = "福利奖励打码量倍数")
  private BigDecimal wealRewordDama;
}

package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动转盘奖品配置DTO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityTurntablePrizeConfigDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "奖项ID")
  private Integer prizeId;

  @Schema(description = "转盘角度最小值")
  private String min;

  @Schema(description = "转盘角度最大值")
  private String max;

  @Schema(description = "奖项名称")
  private String prizeName;

  @Schema(description = "奖金")
  private Double prizeMoney;

  @Schema(description = "中奖名额")
  private Integer quantity;

  @Schema(description = "奖项描述")
  private String prizeDesc;

  @Schema(description = "中将概率")
  private Double probability;
}

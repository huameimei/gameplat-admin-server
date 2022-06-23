package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/17 21:56
 * @desc
 */
@Data
public class TotalMemberWealRewordVO implements Serializable {

  @Schema(description = "升级奖励")
  private BigDecimal totalUpRewardAmount;

  @Schema(description = "周俸禄")
  private BigDecimal totalWeekWageAmount;

  @Schema(description = "月俸禄")
  private BigDecimal totalMonthWageAmount;

  @Schema(description = "生日礼金")
  private BigDecimal totalBirthGiftMoneyAmount;

  @Schema(description = "每月红包")
  private BigDecimal totalMonthRedEnvelopeAmount;
}

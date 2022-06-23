package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/17 21:41
 * @desc 会员红利报表总计
 */
@Data
public class TotalMemberBonusReportVO implements Serializable {

  @Schema(description = "充值优惠")
  private BigDecimal totalRechargeDiscountsAmount;

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

  @Schema(description = "活动红利")
  private BigDecimal totalActivityBonusAmount;

  @Schema(description = "聊天室红包")
  private BigDecimal totalChatRedEnvelopeAmount;
}

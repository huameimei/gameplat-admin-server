package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty("充值优惠")
  private BigDecimal totalRechargeDiscountsAmount;

  @ApiModelProperty("升级奖励")
  private BigDecimal totalUpRewardAmount;

  @ApiModelProperty("周俸禄")
  private BigDecimal totalWeekWageAmount;

  @ApiModelProperty("月俸禄")
  private BigDecimal totalMonthWageAmount;

  @ApiModelProperty("生日礼金")
  private BigDecimal totalBirthGiftMoneyAmount;

  @ApiModelProperty("每月红包")
  private BigDecimal totalMonthRedEnvelopeAmount;

  @ApiModelProperty("活动红利")
  private BigDecimal totalActivityBonusAmount;

  @ApiModelProperty("聊天室红包")
  private BigDecimal totalChatRedEnvelopeAmount;

}

package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author aBen
 * @date 2022/3/16 20:43
 * @desc
 */
@Data
public class MemberBonusReportVO implements Serializable {

  @Schema(description = "会员账号")
  @Excel(name = "会员账号", width = 15, isImportField = "true_st")
  private String userName;

  @Schema(description = "充值优惠")
  @Excel(name = "充值优惠", width = 15, isImportField = "true_st")
  private BigDecimal rechargeDiscountsAmount;

  @Schema(description = "VIP红利金额")
  private BigDecimal vipBonusAmount;

  @Schema(description = "升级奖励")
  @Excel(name = "升级奖励", width = 15, isImportField = "true_st")
  private BigDecimal upRewardAmount;

  @Schema(description = "周俸禄")
  @Excel(name = "周俸禄", width = 15, isImportField = "true_st")
  private BigDecimal weekWageAmount;

  @Schema(description = "月俸禄")
  @Excel(name = "月俸禄", width = 15, isImportField = "true_st")
  private BigDecimal monthWageAmount;

  @Schema(description = "生日礼金")
  @Excel(name = "生日礼金", width = 15, isImportField = "true_st")
  private BigDecimal birthGiftMoneyAmount;

  @Schema(description = "每月红包")
  @Excel(name = "每月红包", width = 15, isImportField = "true_st")
  private BigDecimal monthRedEnvelopeAmount;

  @Schema(description = "活动红利")
  @Excel(name = "活动红利", width = 15, isImportField = "true_st")
  private BigDecimal activityBonusAmount;

  @Schema(description = "聊天室红包")
  @Excel(name = "聊天室红包", width = 15, isImportField = "true_st")
  private BigDecimal chatRedEnvelopeAmount;
}

package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @version 2021-11-25 会员日报表
 */
@Data
@TableName("member_day_report")
public class MemberGameDayReportVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计日期")
  private Date countDate;

  @Schema(description = "转账次数")
  private Integer bankCount;

  @Schema(description = "转账金额")
  private BigDecimal bankMoney;

  @Schema(description = "在线支付次数")
  private Integer onlineCount;

  @Schema(description = "在线支付总金额")
  private BigDecimal onlineMoney;

  @Schema(description = "人工充值次数")
  private Integer handRechCount;

  @Schema(description = "人工充值总金额")
  private BigDecimal handRechMoney;

  @Schema(description = "充值金额总额")
  private BigDecimal totailRechargeAmount;

  @Schema(description = "优惠金额")
  private BigDecimal rechDiscount;

  @Schema(description = "其他优惠金额")
  private BigDecimal otherDiscount;

  @Schema(description = "非正常入款")
  private BigDecimal abnormalIncome;

  @Schema(description = "会员出款次数")
  private Integer withdrawCount;

  @Schema(description = "会员出款金额")
  private BigDecimal withdrawMoney;

  @Schema(description = "人工出款次数")
  private Integer handWithdrawCount;

  @Schema(description = "人工出款金额")
  private BigDecimal handWithdrawMoney;

  @Schema(description = "提现汇总金额")
  private BigDecimal totailWithdrawAmount;

  @Schema(description = "手续费")
  private BigDecimal counterFee;

  @Schema(description = "非正常出款金额")
  private BigDecimal abnormalOutcome;

  @Schema(description = "彩票投注额")
  private BigDecimal lotteryValidAmount;

  @Schema(description = "彩票输赢")
  private BigDecimal lotteryWinAmount;

  @Schema(description = "体育投注额")
  private BigDecimal sportValidAmount;

  @Schema(description = "体育输赢")
  private BigDecimal sportWinAmount;

  @Schema(description = "真人投注额")
  private BigDecimal realValidAmount;

  @Schema(description = "真人输赢")
  private BigDecimal realWinAmount;

  @Schema(description = "真人返水")
  private BigDecimal realWaterAmount;

  @Schema(description = "游戏输赢汇总")
  private BigDecimal totailGameWinAmout;

  @Schema(description = "充提总结余")
  private BigDecimal totalRWAmount;
}

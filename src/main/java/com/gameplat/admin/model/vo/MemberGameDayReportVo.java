package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "统计日期")
  private Date countDate;

  @ApiModelProperty(value = "转账次数")
  private Integer bankCount;

  @ApiModelProperty(value = "转账金额")
  private BigDecimal bankMoney;

  @ApiModelProperty(value = "在线支付次数")
  private Integer onlineCount;

  @ApiModelProperty(value = "在线支付总金额")
  private BigDecimal onlineMoney;

  @ApiModelProperty(value = "人工充值次数")
  private Integer handRechCount;

  @ApiModelProperty(value = "人工充值总金额")
  private BigDecimal handRechMoney;

  @ApiModelProperty(value = "充值金额总额")
  private BigDecimal totailRechargeAmount;

  @ApiModelProperty(value = "优惠金额")
  private BigDecimal rechDiscount;

  @ApiModelProperty(value = "其他优惠金额")
  private BigDecimal otherDiscount;

  @ApiModelProperty(value = "非正常入款")
  private BigDecimal abnormalIncome;

  @ApiModelProperty(value = "会员出款次数")
  private Integer withdrawCount;

  @ApiModelProperty(value = "会员出款金额")
  private BigDecimal withdrawMoney;

  @ApiModelProperty(value = "人工出款次数")
  private Integer handWithdrawCount;

  @ApiModelProperty(value = "人工出款金额")
  private BigDecimal handWithdrawMoney;

  @ApiModelProperty(value = "提现汇总金额")
  private BigDecimal totailWithdrawAmount;

  @ApiModelProperty(value = "手续费")
  private BigDecimal counterFee;

  @ApiModelProperty(value = "非正常出款金额")
  private BigDecimal abnormalOutcome;

  @ApiModelProperty(value = "彩票投注额")
  private BigDecimal lotteryValidAmount;

  @ApiModelProperty(value = "彩票输赢")
  private BigDecimal lotteryWinAmount;

  @ApiModelProperty(value = "体育投注额")
  private BigDecimal sportValidAmount;

  @ApiModelProperty(value = "体育输赢")
  private BigDecimal sportWinAmount;

  @ApiModelProperty(value = "真人投注额")
  private BigDecimal realValidAmount;

  @ApiModelProperty(value = "真人输赢")
  private BigDecimal realWinAmount;

  @ApiModelProperty(value = "真人返水")
  private BigDecimal realWaterAmount;

  @ApiModelProperty(value = "游戏输赢汇总")
  private BigDecimal totailGameWinAmout;

  @ApiModelProperty(value = "充提总结余")
  private BigDecimal totalRWAmount;
}

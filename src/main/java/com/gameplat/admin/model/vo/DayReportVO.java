package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员日报表出参
 * @date 2021/12/31
 */
@Data
public class DayReportVO implements Serializable {

  private static final long serialVersionUID = -3196207776088644481L;

  @Schema(description = "统计日期")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date statTime;

  @Schema(description = "转账汇款金额")
  private Double bankMoney;

  @Schema(description = "转账汇款次数")
  private Integer bankCount;

  @Schema(description = "在线支付金额")
  private Double onlineMoney;

  @Schema(description = "在线支付次数")
  private Integer onlineCount;

  @Schema(description = "人工充值金额")
  private Double handRechMoney;

  @Schema(description = "人工充值次数")
  private Integer handRechCount;

  @Schema(description = "充值汇总金额")
  private Double totalRechargeAmount;

  @Schema(description = "充值汇总次数")
  private Integer totalRechargeCount;

  @Schema(description = "充值优惠")
  private Double rechDiscount;

  @Schema(description = "其他优惠")
  private Double otherDiscount;

  @Schema(description = "非正常入款")
  private Double exceptionRechargeAmount;

  @Schema(description = "会员取款金额")
  private Double totalWithdrawAmount;

  @Schema(description = "会员取款次数")
  private Integer totalWithdrawCount;

  @Schema(description = "人工提现金额")
  private Double handWithdrawMoney;

  @Schema(description = "人工提现次数")
  private Integer handWithdrawCount;

  @Schema(description = "提现汇总金额")
  private Double withdrawMoney;

  @Schema(description = "提现汇总次数")
  private Integer withdrawCount;

  @Schema(description = "手续费")
  private Double counterFee;

  @Schema(description = "非正常出款")
  private Double exceptionWithdrawAmount;

  @Schema(description = "彩票投注额")
  private Double cpBetMoney;

  @Schema(description = "彩票输赢")
  private Double cpWinMoney;

  @Schema(description = "体育投注额")
  private Double spBetMoney;

  @Schema(description = "体育输赢")
  private Double spWinMoney;

  @Schema(description = "真人投注额")
  private Double liveBetMoney;

  @Schema(description = "真人输赢")
  private Double liveWinMoney;

  @Schema(description = "真人返水")
  private Double liveRebateMoney;

  @Schema(description = "游戏输赢汇总")
  private Double totalWinOrcloseMoney;

  @Schema(description = "代理返点")
  private Double dlRebateMoney;

  @Schema(description = "代理分红")
  private Double dlBonuus;

  @Schema(description = "代理日工资")
  private Double dlDayWage;

  @Schema(description = "层层代理分红")
  private Double dlRatio;

  @Schema(description = "团队分红")
  private Double teamWage;

  @Schema(description = "出入款差")
  private Double inAndOut;

  @Schema(description = "公司收入汇总")
  private Double totalRemainAmount;
}

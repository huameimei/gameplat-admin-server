package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb 投注分析dto @Date 2022/2/24 22:15 @Version 1.0
 */
@Data
public class MemberbetAnalysisVo implements Serializable {

  /** 彩票注单数 */
  private Integer lotteryOrderCount;

  /** 体育注单数 */
  private Integer sportOrderCount;

  /** '真人注单数' */
  private Integer realOrderCount;

  /** '彩票中奖订单数' */
  private Integer lotteryWinOrderCount;

  /** '体育中奖订单数' */
  private Integer sportWinOrderCount;

  /** '真人中奖订单数' */
  private Integer realWinOrderCount;

  /** '彩票有效投注额' */
  private BigDecimal lotteryValidAmount;

  /** '体育有效投注额' */
  private BigDecimal sportValidAmount;

  /** '真人有效投注额' */
  private BigDecimal realValidAmount;

  /** ''彩票输赢金额'' */
  private BigDecimal lotteryWinAmount;

  /** '体育输赢金额' */
  private BigDecimal sportWinAmount;

  /** '真人输赢金额' */
  private BigDecimal realWinAmount;

  /** 彩票返水金额 */
  private BigDecimal lotteryWaterAmount;

  /** 体育返水金额 */
  private BigDecimal sportWaterAmount;

  /** 真人返水金额 */
  private BigDecimal realWaterAmount;

  /** 彩票盈利率 */
  private BigDecimal lotteryProfitRate;

  /** 体育盈利率 */
  private BigDecimal sportProfitRate;

  /** 真人盈利率 */
  private BigDecimal realProfitRate;

  /** '彩票中奖率' */
  private BigDecimal lotteryWinRate;

  /** '体育中奖率' */
  private BigDecimal sportWinRate;

  /** '真人中奖率' */
  private BigDecimal realWinRate;

  /** 全部输赢 */
  private BigDecimal allWinAmount;

  public MemberbetAnalysisVo() {

    this.lotteryOrderCount = 0;
    this.sportOrderCount = 0;
    this.realOrderCount = 0;
    this.lotteryWinOrderCount = 0;
    this.sportWinOrderCount = 0;
    this.realWinOrderCount = 0;
    this.lotteryValidAmount = new BigDecimal(0);
    this.sportValidAmount = new BigDecimal(0);
    this.realValidAmount = new BigDecimal(0);
    this.lotteryWinAmount = new BigDecimal(0);
    this.sportWinAmount = new BigDecimal(0);
    this.realWinAmount = new BigDecimal(0);
    this.lotteryWaterAmount = new BigDecimal(0);
    this.sportWaterAmount = new BigDecimal(0);
    this.realWaterAmount = new BigDecimal(0);
    this.lotteryProfitRate = new BigDecimal(0);
    this.sportProfitRate = new BigDecimal(0);
    this.realProfitRate = new BigDecimal(0);
    this.lotteryWinRate = new BigDecimal(0);
    this.sportWinRate = new BigDecimal(0);
    this.realWinRate = new BigDecimal(0);
  }

  public BigDecimal getAllWinAmount() {

    return lotteryWinAmount.add(sportWinAmount).add(realWinAmount);
  }
}

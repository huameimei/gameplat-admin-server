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


  /**
   * 电子注单数
   */
  private Integer eGameOrderCount;


  /**
   * 电竞注单数
   */
  private Integer eSportOrderCount;


  /**
   * 捕鱼注单数
   */
  private Integer hunterOrderCount;


  /**
   * 棋牌注单数
   */
  private Integer chessOrderCount;



  /** '彩票中奖订单数' */
  private Integer lotteryWinOrderCount;

  /** '体育中奖订单数' */
  private Integer sportWinOrderCount;

  /** '真人中奖订单数' */
  private Integer realWinOrderCount;

  /**
   * '电子中奖订单数'
   */
  private Integer eGameWinOrderCoun;

  /**
   * '电竞中奖订单数'
   */
  private Integer eSportWinOrderCount;

  /**
   * '捕鱼中奖订单数'
   */
  private Integer hunterWinOrderCount;

  /**
   * '棋牌中奖订单数'
   */
  private Integer chessWinOrderCount;



  /** '彩票有效投注额' */
  private BigDecimal lotteryValidAmount;

  /** '体育有效投注额' */
  private BigDecimal sportValidAmount;

  /**
   * '真人有效投注额'
   */
  private BigDecimal realValidAmount;

  /**
   * '电子有效投注额'
   */
  private BigDecimal eGameValidAmount;

  /**
   * '电竞有效投注额'
   */
  private BigDecimal eSportValidAmount;

  /**
   * '捕鱼有效投注额'
   */
  private BigDecimal hunterValidAmount;

  /** '棋牌有效投注额' */
  private BigDecimal chessValidAmount;



  /** ''彩票输赢金额'' */
  private BigDecimal lotteryWinAmount;

  /** '体育输赢金额' */
  private BigDecimal sportWinAmount;

  /**
   * '真人输赢金额'
   */
  private BigDecimal realWinAmount;

  /**
   * '电子输赢金额'
   */
  private BigDecimal eGameWinAmount;

  /**
   * '电竞输赢金额'
   */
  private BigDecimal eSportWinAmount;

  /** '捕鱼输赢金额' */
  private BigDecimal hunterWinAmount;

  /** '棋牌输赢金额' */
  private BigDecimal chessWinAmount;


  /** 彩票返水金额 */
  private BigDecimal lotteryWaterAmount;

  /**
   * 体育返水金额
   */
  private BigDecimal sportWaterAmount;

  /**
   * 真人返水金额
   */
  private BigDecimal realWaterAmount;

  /**
   * 电子返水金额
   */
  private BigDecimal eGameWaterAmount;

  /**
   * 电竞返水金额
   */
  private BigDecimal eSportWaterAmount;

  /**
   * 捕鱼返水金额
   */
  private BigDecimal hunterWaterAmount;

  /** 棋牌返水金额 */
  private BigDecimal chessWaterAmount;



  /** 彩票盈利率 */
  private BigDecimal lotteryProfitRate;

  /** 体育盈利率 */
  private BigDecimal sportProfitRate;

  /** 真人盈利率 */
  private BigDecimal realProfitRate;

  /**
   * 电子盈利率
   */
  private BigDecimal eGameProfitRate;

  /**
   * 电竞盈利率
   */
  private BigDecimal eSportProfitRate;

  /**
   * 捕鱼盈利率
   */
  private BigDecimal hunterProfitRate;

  /**
   * 棋牌盈利率
   */
  private BigDecimal chessProfitRate;




  /** '彩票中奖率' */
  private BigDecimal lotteryWinRate;

  /**
   * '体育中奖率'
   */
  private BigDecimal sportWinRate;

  /**
   * '真人中奖率'
   */
  private BigDecimal realWinRate;

  /**
   * '电子中奖率'
   */
  private BigDecimal eGameWinRate;

  /** '电竞中奖率' */
  private BigDecimal eSportWinRate;

  /** '捕鱼中奖率' */
  private BigDecimal hunterWinRate;

  /** '棋牌中奖率' */
  private BigDecimal chessWinRate;


  /**
   * 全部输赢
   */
  private BigDecimal allWinAmount;


  public MemberbetAnalysisVo() {
    this.lotteryOrderCount = 0;
    this.sportOrderCount = 0;
    this.realOrderCount = 0;
    this.eGameOrderCount = 0;
    this.eSportOrderCount = 0;
    this.hunterOrderCount = 0;
    this.chessOrderCount = 0;
    this.lotteryWinOrderCount = 0;
    this.sportWinOrderCount = 0;
    this.realWinOrderCount = 0;
    this.eGameWinOrderCoun = 0;
    this.eSportWinOrderCount = 0;
    this.hunterWinOrderCount = 0;
    this.chessWinOrderCount = 0;
    this.lotteryValidAmount = BigDecimal.ZERO;
    this.sportValidAmount = BigDecimal.ZERO;
    this.realValidAmount = BigDecimal.ZERO;
    this.eGameValidAmount = BigDecimal.ZERO;
    this.eSportValidAmount = BigDecimal.ZERO;
    this.hunterValidAmount = BigDecimal.ZERO;
    this.chessValidAmount = BigDecimal.ZERO;
    this.lotteryWinAmount = BigDecimal.ZERO;
    this.sportWinAmount = BigDecimal.ZERO;
    this.realWinAmount = BigDecimal.ZERO;
    this.eGameWinAmount = BigDecimal.ZERO;
    this.eSportWinAmount = BigDecimal.ZERO;
    this.hunterWinAmount = BigDecimal.ZERO;
    this.chessWinAmount = BigDecimal.ZERO;
    this.lotteryWaterAmount = BigDecimal.ZERO;
    this.sportWaterAmount = BigDecimal.ZERO;
    this.realWaterAmount = BigDecimal.ZERO;
    this.eGameWaterAmount = BigDecimal.ZERO;
    this.eSportWaterAmount = BigDecimal.ZERO;
    this.hunterWaterAmount = BigDecimal.ZERO;
    this.chessWaterAmount = BigDecimal.ZERO;
    this.lotteryProfitRate = BigDecimal.ZERO;
    this.sportProfitRate = BigDecimal.ZERO;
    this.realProfitRate = BigDecimal.ZERO;
    this.eGameProfitRate = BigDecimal.ZERO;
    this.eSportProfitRate = BigDecimal.ZERO;
    this.hunterProfitRate = BigDecimal.ZERO;
    this.chessProfitRate = BigDecimal.ZERO;
    this.lotteryWinRate = BigDecimal.ZERO;
    this.sportWinRate = BigDecimal.ZERO;
    this.realWinRate = BigDecimal.ZERO;
    this.eGameWinRate = BigDecimal.ZERO;
    this.eSportWinRate = BigDecimal.ZERO;
    this.hunterWinRate = BigDecimal.ZERO;
    this.chessWinRate = BigDecimal.ZERO;
    this.allWinAmount = BigDecimal.ZERO;
  }

  public BigDecimal getAllWinAmount() {

    return lotteryWinAmount
        .add(sportWinAmount)
        .add(realWinAmount)
        .add(eGameWinAmount)
        .add(sportWinAmount)
        .add(hunterWinAmount)
        .add(chessWinAmount);
  }
}

package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameBetReportVO implements Serializable {

  /**
   * 报表时间
   */
  private String reportTime;

  /**
   *账号
   */
  private String account;

  /**
   * 游戏大类code
   */
  private String gameCode;

  /**
   * 游戏大类
   */
  private String gameName;

  /**
   * 平台编码
   */
  private String platformCode;


  /**
   * 平台名称
   */
  private String platformName;


  /**
   * 游戏局数
   */
  private Integer gameCount;


  /**
   * 投注金额
   */
  private BigDecimal betAmount;

  /**
   * 有效投注额
   */
  private BigDecimal validAmount;

  /**
   * 中奖金额
   */
  private BigDecimal winAmount;

}

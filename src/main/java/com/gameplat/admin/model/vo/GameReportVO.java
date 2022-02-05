package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameReportVO implements Serializable {
  /**
   * 平台
   */
  private String platformCode;

  /**
   * 平台
   */
  private String platformName;

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

  /**
   * 会员人数
   */
  private Integer userNumber;

  /**
   * 游戏局数
   */
  private Integer gameCount;

  /**
   * 返水金额
   */
  private BigDecimal rebateMoney;

  /**
   * 公司输赢
   */
  private BigDecimal companyAmount;
}

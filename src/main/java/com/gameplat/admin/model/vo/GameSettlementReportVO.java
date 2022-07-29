package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameSettlementReportVO implements Serializable {

  /** 平台 */
  private String platformCode;

  /** 平台 */
  @Excel(name = "平台名称", width = 20)
  private String platformName;

  /** 游戏大类 */
  private String gameTypeCode;

  /** 游戏大类名称 */
  private String gameTypeName;

  /** 会员人数 */
  @Excel(name = "下注人数", width = 10)
  private Integer userNumber;

  /** 游戏局数 */
  @Excel(name = "下注笔数", width = 10)
  private Integer gameCount;

  /** 投注金额 */
  @Excel(name = "投注金额", width = 20)
  private BigDecimal betAmount;

  /** 有效投注额 */
  @Excel(name = "有效投注额", width = 20)
  private BigDecimal validAmount;

  /** 中奖金额 */
  @Excel(name = "会员输赢", width = 20)
  private BigDecimal winAmount;

  /** 返水金额 */
  @Excel(name = "返水金额", width = 20)
  private BigDecimal rebateMoney;

  /** 公司输赢 */
  @Excel(name = "公司输赢", width = 20)
  private BigDecimal companyAmount;
}

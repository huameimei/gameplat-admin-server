package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameBetRecordExportVO implements Serializable {

  /** 用户名 */
  @Excel(name = "会员名称", width = 20)
  private String account;

  /** 游戏分类 */
  @Excel(name = "游戏分类", width = 50)
  private String gameKind;

  /** 单号 */
  @Excel(name = "游戏单号", width = 50)
  private String billNo;

  /** 游戏名称 */
  @Excel(name = "游戏名称", width = 50)
  private String gameName;

  /** 投注金额 */
  @Excel(name = "投注金额", width = 20)
  private BigDecimal betAmount;

  /** 有效投注额 */
  @Excel(name = "有效投注额", width = 20)
  private BigDecimal validAmount;

  /** 输赢金额 */
  @Excel(name = "输赢金额", width = 20)
  private BigDecimal winAmount;

  /** 投注时间 */
  @Excel(name = "投注时间", width = 20, exportFormat = "yyyy-MM-dd hh:mm:ss")
  private String betTime;

  /** 结算时间 */
  @Excel(name = "结算时间", width = 20, exportFormat = "yyyy-MM-dd hh:mm:ss")
  private String settleTime;

  /** 报表统计时间 */
  @Excel(name = "报表时间", width = 20, exportFormat = "yyyy-MM-dd hh:mm:ss")
  private String statTime;
}

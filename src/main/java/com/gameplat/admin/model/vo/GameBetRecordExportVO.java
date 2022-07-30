package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameBetRecordExportVO implements Serializable {

  /** 用户名 */
  @Excel(name = "会员账号", width = 20, orderNum = "1")
  private String account;

  /** 游戏分类 */
  private String gameKind;

  /** 单号 */
  @Excel(name = "订单号", width = 40, orderNum = "2")
  private String billNo;

  /** 游戏名称 */
  @Excel(name = "游戏名称", width = 30, orderNum = "4")
  private String gameName;

  /** 投注金额 */
  @Excel(name = "投注金额", width = 15, orderNum = "5")
  private BigDecimal betAmount;

  /** 有效投注额 */
  @Excel(name = "有效投注额", width = 15, orderNum = "6")
  private BigDecimal validAmount;

  /** 输赢金额 */
  @Excel(name = "输赢", width = 15, orderNum = "7")
  private BigDecimal winAmount;

  /** 投注时间 */
  @Excel(name = "投注时间", width = 25, orderNum = "8")
  private String betTime;

  /** 结算时间 */
  @Excel(name = "结算时间", width = 25, orderNum = "9")
  private String settleTime;

  /** 报表统计时间 */
  @Excel(name = "报表时间", width = 25, orderNum = "10")
  private String statTime;
}

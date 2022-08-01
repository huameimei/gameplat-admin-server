package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cc
 * @version 2022-07-27 用户活跃度
 */
@Data
public class MemberLiveReportExportVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Excel(orderNum = "1", name = "会员账户", width = 15)
  private String account;

  @Excel(orderNum = "2", name = "真实姓名")
  private String realName;

  @Excel(orderNum = "3", name = "充值层级", width = 8)
  private String userLevel;

  @Excel(orderNum = "4", name = "充值层级", width = 15)
  private String balance;

  @Excel(orderNum = "5", name = "注册时间", width = 15)
  private String registerTime;

  @Excel(orderNum = "6", name = "代理层级", width = 15)
  private String memberTypeLevel;

  @Excel(orderNum = "7", name = "代理账号", width = 15)
  private String parentName;

  @Excel(orderNum = "8", name = "代理路径", width = 20)
  private String agentPath;

  @Excel(orderNum = "9", name = "充值次数", width = 7)
  private Long rechargeCount = 0L;

  @Excel(orderNum = "10", name = "充值金额", width = 15)
  private BigDecimal rechargeMoney = new BigDecimal("0");

  @Excel(orderNum = "11", name = "充值优惠", width = 15)
  private BigDecimal rechDiscount = new BigDecimal("0");

  @Excel(orderNum = "12", name = "其它优惠", width = 15)
  private BigDecimal otherDiscount = new BigDecimal("0");

  @Excel(orderNum = "13", name = "最后充值时间", width = 18)
  private String lastRechTime;

  @Excel(orderNum = "14", name = "提现次数", width = 7)
  private Long withdrawCount = 0L;

  @Excel(orderNum = "15", name = "提现金额", width = 15)
  private BigDecimal withdrawMoney = new BigDecimal("0");

  @Excel(orderNum = "16", name = "手续费", width = 15)
  private BigDecimal counterFee = new BigDecimal("0");

  @Excel(orderNum = "17", name = "彩票有效投注", width = 15)
  private BigDecimal lotteryValidAmount = new BigDecimal("0");

  @Excel(orderNum = "18", name = "彩票输赢", width = 15)
  private BigDecimal lotteryWinAmount = new BigDecimal("0");

  @Excel(orderNum = "19", name = "彩票代理返点", width = 15)
  private BigDecimal backWaterAmount = new BigDecimal("0");

  @Excel(orderNum = "20", name = "体育有效投注", width = 15)
  private BigDecimal sportValidAmount = new BigDecimal("0");

  @Excel(orderNum = "21", name = "体育输赢", width = 15)
  private BigDecimal sportWinAmount = new BigDecimal("0");

  @Excel(orderNum = "22", name = "真人有效投注", width = 15)
  private BigDecimal realValidAmount = new BigDecimal("0");

  @Excel(orderNum = "23", name = "真人输赢", width = 15)
  private BigDecimal realWinAmount = new BigDecimal("0");

  @Excel(orderNum = "24", name = "返水金额", width = 12)
  private BigDecimal waterAmount = new BigDecimal("0");

  @Excel(orderNum = "25", name = "有效投注汇总", width = 15)
  private BigDecimal validAmount = new BigDecimal("0");

  @Excel(orderNum = "26", name = "输赢金额汇总", width = 15)
  private BigDecimal winAmount = new BigDecimal("0");

  @Excel(orderNum = "27", name = "层层代理分红", width = 12)
  private BigDecimal realDivideAmount = new BigDecimal("0");

  @Excel(orderNum = "28", name = "代理工资", width = 12)
  private BigDecimal salaryAmount = new BigDecimal("0");

  @Excel(orderNum = "29", name = "VIP福利", width = 12)
  private BigDecimal vipRewordAmount = new BigDecimal("0");

  @Excel(orderNum = "30", name = "活动金额", width = 12)
  private BigDecimal activityAmount = new BigDecimal("0");

  @Excel(orderNum = "31", name = "聊天室红包", width = 12)
  private BigDecimal chatAmount = new BigDecimal("0");

  @Excel(orderNum = "32", name = "公司输赢汇总", width = 12)
  private BigDecimal companyWin = new BigDecimal("0");
}

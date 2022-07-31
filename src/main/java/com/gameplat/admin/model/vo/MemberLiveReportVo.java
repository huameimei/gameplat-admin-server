package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cc
 * @version 2022-07-27 用户活跃度
 */
@Data
public class MemberLiveReportVo implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 会员名 */
  private String account;
  /** 真实姓名 */
  private String realName;
  /** 充值层级 */
  private String userLevel;
  /** 账户余额 */
  private String balance;
  /** 注册时间 */
  private String registerTime;
  /** 代理层级和用户类型 */
  private String memberTypeLevel;
  /** 上级代理 */
  private String parentName;
  /** 代理路径 */
  private String agentPath;
  /** 充值次数 */
  private Long rechargeCount = 0L;
  /** 充值金额 */
  private BigDecimal rechargeMoney = new BigDecimal("0");
  /** 充值优惠 */
  private BigDecimal rechDiscount = new BigDecimal("0");
  /** 其它优惠 */
  private BigDecimal otherDiscount = new BigDecimal("0");
  /** 最后充值时间 */
  private String lastRechTime;
  /** 提现次数 */
  private Long withdrawCount = 0L;
  /** 提现金额 */
  private BigDecimal withdrawMoney = new BigDecimal("0");
  /** 手续费 */
  private BigDecimal counterFee = new BigDecimal("0");
  /** 彩票有效投注 */
  private BigDecimal lotteryValidAmount = new BigDecimal("0");
  /** 彩票输赢 */
  private BigDecimal lotteryWinAmount = new BigDecimal("0");
  /** 彩票代理返点 */
  private BigDecimal backWaterAmount = new BigDecimal("0");
  /** 体育有效投注 */
  private BigDecimal sportValidAmount = new BigDecimal("0");
  /** 体育输赢 */
  private BigDecimal sportWinAmount = new BigDecimal("0");
  /** 真人（真人电子棋牌电竞捕鱼）有效投注 */
  private BigDecimal realValidAmount = new BigDecimal("0");
  /** 真人（真人电子棋牌电竞捕鱼）输赢 */
  private BigDecimal realWinAmount = new BigDecimal("0");
  /** 返水金额 */
  private BigDecimal waterAmount = new BigDecimal("0");
  /** 有效投注汇总 */
  private BigDecimal validAmount = new BigDecimal("0");
  /** 输赢金额汇总 */
  private BigDecimal winAmount = new BigDecimal("0");
  /** 层层代理分红 */
  private BigDecimal realDivideAmount = new BigDecimal("0");
  /** 代理工资 */
  private BigDecimal salaryAmount = new BigDecimal("0");
  /** VIP福利 */
  private BigDecimal vipRewordAmount = new BigDecimal("0");
  /** 活动金额 */
  private BigDecimal activityAmount = new BigDecimal("0");
  /** 聊天室红包 */
  private BigDecimal chatAmount = new BigDecimal("0");
  /** 公司输赢汇总 */
  private BigDecimal companyWin = new BigDecimal("0");
}

package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @version 2021-11-25 会员日报表
 */
@Data
@TableName("member_day_report")
public class MemberDayReportVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计日期")
  private Date countDate;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "代理账号")
  private String parentName;

  @Schema(description = "vip等级")
  private String vipLevel;

  @Schema(description = "用户层级")
  private String userLevel;

  @Schema(description = "代理 层级")
  private Integer agentLevel;

  @Schema(description = "用户类型")
  private Integer userType;

  @Schema(description = "首存人数")
  private Integer firstRechargeNum;

  @Schema(description = "充值人数")
  private Integer rechargeNum;

  @Schema(description = "充值次数")
  private int rechargeCount;

  @Schema(description = "充值金额")
  private BigDecimal rechargeAmount;

  @Schema(description = "优惠金额")
  private BigDecimal discountAmount;

  @Schema(description = "彩金")
  private BigDecimal jackpotAmount;

  @Schema(description = "提现人数")
  private Integer withdrawNum;

  @Schema(description = "提现次数")
  private int withdrawCount;

  @Schema(description = "提现金额")
  private BigDecimal withdrawAmount;

  @Schema(description = "手续费")
  private BigDecimal feeAmount;

  @Schema(description = "投注人数")
  private Integer betNum;

  @Schema(description = "红利人数")
  private Integer bonusNum;

  @Schema(description = "投注额")
  private BigDecimal betAmount;

  @Schema(description = "有效投注额")
  private BigDecimal validAmount;

  @Schema(description = "输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "返水金额")
  private BigDecimal waterAmount;

  @Schema(description = "首冲金额")
  private BigDecimal firstRechargeAmount;

  @Schema(description = "首提金额")
  private BigDecimal firstWithdrawAmount;

  @Schema(description = "下级代理人数")
  private int agentNum;

  @Schema(description = "总下级代理人数")
  private int agentTotalNum;

  @Schema(description = "下级会员人数")
  private int memberNum;

  @Schema(description = "总下级会员人数")
  private int memberTotalNum;

  @Schema(description = "派彩金额")
  private BigDecimal payOutAmount;

  @Schema(description = "红利金额")
  private BigDecimal bonusAmount;

  @Schema(description = "公司收入")
  private BigDecimal companyIncome;

  @Schema(description = "注册会员数")
  private Integer registerNum;

  @Schema(description = "注册代理数")
  private Integer registerAgentNum;

  @Schema(description = "VIP福利")
  private BigDecimal vipRewordAmount;

  @Schema(description = "活动礼金")
  private BigDecimal activityAmount;

  @Schema(description = "聊天室红包")
  private BigDecimal chatAmount;

  public MemberDayReportVo() {
    this.rechargeCount = 0;
    this.firstRechargeNum = 0;
    this.rechargeNum = 0;
    this.withdrawNum = 0;
    this.betNum = 0;
    this.bonusNum = 0;
    this.agentNum = 0;
    this.agentTotalNum = 0;
    this.memberNum = 0;
    this.memberTotalNum = 0;
    this.withdrawCount = 0;
    this.registerNum = 0;
    this.registerAgentNum = 0;
    this.rechargeAmount = new BigDecimal(0);
    this.discountAmount = new BigDecimal(0);
    this.jackpotAmount = new BigDecimal(0);
    this.withdrawAmount = new BigDecimal(0);
    this.feeAmount = new BigDecimal(0);
    this.betAmount = new BigDecimal(0);
    this.validAmount = new BigDecimal(0);
    this.winAmount = new BigDecimal(0);
    this.waterAmount = new BigDecimal(0);
    this.firstRechargeAmount = new BigDecimal(0);
    this.firstWithdrawAmount = new BigDecimal(0);
    this.payOutAmount = new BigDecimal(0);
    this.bonusAmount = new BigDecimal(0);
    this.companyIncome = new BigDecimal(0);
    this.vipRewordAmount = new BigDecimal(0);
    this.activityAmount = new BigDecimal(0);
    this.chatAmount = new BigDecimal(0);
  }
}

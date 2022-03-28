package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "统计日期")
  private Date countDate;

  @ApiModelProperty(value = "会员账号")
  private String userName;

  @ApiModelProperty(value = "代理账号")
  private String parentName;

  @ApiModelProperty(value = "vip等级")
  private String vipLevel;

  @ApiModelProperty(value = "用户层级")
  private String userLevel;

  @ApiModelProperty(value = "代理 层级")
  private Integer agentLevel;

  @ApiModelProperty(value = "用户类型")
  private Integer userType;

  @ApiModelProperty(value = "首存人数")
  private Integer firstRechargeNum;

  @ApiModelProperty(value = "充值人数")
  private Integer rechargeNum;

  @ApiModelProperty(value = "充值次数")
  private int rechargeCount;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal rechargeAmount;

  @ApiModelProperty(value = "优惠金额")
  private BigDecimal discountAmount;

  @ApiModelProperty(value = "彩金")
  private BigDecimal jackpotAmount;

  @ApiModelProperty(value = "提现人数")
  private Integer withdrawNum;

  @ApiModelProperty(value = "提现次数")
  private int withdrawCount;

  @ApiModelProperty(value = "提现金额")
  private BigDecimal withdrawAmount;

  @ApiModelProperty(value = "手续费")
  private BigDecimal feeAmount;

  @ApiModelProperty(value = "投注人数")
  private Integer betNum;

  @ApiModelProperty(value = "红利人数")
  private Integer bonusNum;

  @ApiModelProperty(value = "投注额")
  private BigDecimal betAmount;

  @ApiModelProperty(value = "有效投注额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "输赢金额")
  private BigDecimal winAmount;

  @ApiModelProperty(value = "返水金额")
  private BigDecimal waterAmount;

  @ApiModelProperty(value = "首冲金额")
  private BigDecimal firstRechargeAmount;

  @ApiModelProperty(value = "首提金额")
  private BigDecimal firstWithdrawAmount;

  @ApiModelProperty(value = "下级代理人数")
  private int agentNum;

  @ApiModelProperty(value = "总下级代理人数")
  private int agentTotalNum;

  @ApiModelProperty(value = "下级会员人数")
  private int memberNum;

  @ApiModelProperty(value = "总下级会员人数")
  private int memberTotalNum;

  @ApiModelProperty(value = "派彩金额")
  private BigDecimal payOutAmount;

  @ApiModelProperty(value = "红利金额")
  private BigDecimal bonusAmount;

  @ApiModelProperty(value = "公司收入")
  private BigDecimal companyIncome;

  @ApiModelProperty(value = "注册会员数")
  private Integer registerNum;

  @ApiModelProperty(value = "注册代理数")
  private Integer registerAgentNum;

  @ApiModelProperty(value = "VIP福利")
  private BigDecimal vipRewordAmount;

  @ApiModelProperty(value = "活动礼金")
  private BigDecimal activityAmount;

  @ApiModelProperty(value = "聊天室红包")
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

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** 会员活跃度VO对象 */
@Data
public class MemberActivationVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "用户id")
  private Integer userId;

  @Schema(description = "用户名")
  private String username;

  @Schema(description = "真实姓名")
  private String idName;

  @Schema(description = "余额")
  private BigDecimal balance;

  @Schema(description = "真币加密串")
  private String goodMoney;

  @Schema(description = "注册时间")
  private String createTime;

  @Schema(description = "活跃时间")
  private String activationTime;

  @Schema(description = "充值次数")
  private Integer rechargeCount;

  @Schema(description = "充值金额")
  private BigDecimal rechargeMoney;

  @Schema(description = "充值优惠")
  private BigDecimal rechargeDiscounts;

  @Schema(description = "其它优惠")
  private BigDecimal otherDiscounts;

  @Schema(description = "提现次数")
  private Integer withdrawCount;

  @Schema(description = "提现金额")
  private BigDecimal withdrawMoney;

  @Schema(description = "游戏输赢汇总")
  private BigDecimal gameWLSum;

  @Schema(description = "游戏天数")
  private Integer gameDays;

  @Schema(description = "公司收入汇总")
  private BigDecimal corporationIncomeSum;
}

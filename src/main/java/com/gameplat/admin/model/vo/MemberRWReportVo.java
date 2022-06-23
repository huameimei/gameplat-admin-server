package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @version 2021-11-25 会员日报表
 */
@Data
public class MemberRWReportVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "会员账号")
  private String userName;

  /*@Schema(description = "代理账号")
  	private String parentName;
  */

  @Schema(description = "转账次数")
  private Integer bankCount;

  @Schema(description = "转账金额")
  private BigDecimal bankMoney;

  @Schema(description = "在线支付次数")
  private Integer onlineCount;

  @Schema(description = "在线支付总金额")
  private BigDecimal onlineMoney;

  @Schema(description = "人工充值次数")
  private Integer handRechCount;

  @Schema(description = "人工充值总金额")
  private BigDecimal handRechMoney;

  @Schema(description = "首次充值金额")
  private BigDecimal firstRechMoney;

  @Schema(description = "优惠金额")
  private BigDecimal rechDiscount;

  @Schema(description = "其他优惠金额")
  private BigDecimal otherDiscount;

  @Schema(description = "会员出款次数")
  private Integer withdrawCount;

  @Schema(description = "会员出款金额")
  private BigDecimal withdrawMoney;

  @Schema(description = "人工出款次数")
  private Integer handWithdrawCount;

  @Schema(description = "人工出款金额")
  private BigDecimal handWithdrawMoney;

  @Schema(description = "首次出款金额")
  private BigDecimal firstWithdrawMoney;

  @Schema(description = "手续费")
  private BigDecimal counterFee;

  @Schema(description = "充值金额总额")
  private BigDecimal totailRechargeAmount;

  @Schema(description = "优惠金额总额")
  private BigDecimal totailDiscoutAmount;

  @Schema(description = "提现金额")
  private BigDecimal totailWithdrawAmount;

  @Schema(description = "充提总结余")
  private BigDecimal totalRWAmount;

  @Schema(description = "代理账号")
  private String superAccount;
}

package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @date 2021/11/27
 */
@Data
public class CashConfigVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "提现比例 1提现币等于n人民币")
  private BigDecimal rate;

  @Schema(description = "提现类型（1日结 2周结 3月结）")
  private Integer cycleType;

  @Schema(description = "提现货币需要大于n才能提现")
  private BigDecimal withdrawNeedNum;

  @Schema(description = "提现货币需要大于n才能提现开关 0关闭 1开启")
  private Integer withdrawNeedLimit;

  @Schema(description = "单比提现金额需要大于n才能提现")
  private BigDecimal withdrawOneAmount;

  @Schema(description = "单比提现金额需要大于n才能提现 0关闭 1开启")
  private Integer withdrawOneOpen;

  @Schema(description = "是否需要实名认证 0不需要 1需要")
  private Integer needReal;

  @Schema(description = "是否需要提现银行卡 0不需要 1需要")
  private Integer needBankCard;

  @Schema(description = "是否需要已开通直播 0不需要  1需要")
  private Integer needOpenLive;

  @Schema(description = "单笔提现金额最高限制")
  private BigDecimal withdrawOneMaxAmount;

  @Schema(description = "提现次数限制")
  private BigDecimal withdrawCount;

  @Schema(description = "单日提现金额限制")
  private BigDecimal withdrawMaxAmount;

  @Schema(description = "单日剩余限额")
  private BigDecimal withdrawtRanslateAmount;

  @Schema(description = "开启提现短信验证 0：开启 1：未开启")
  private Integer withdrawVal;

  /** 是否重复提交 */
  private String isRepeat;

  /** usdt开关 */
  @Schema(description = "usdt开关  0 关闭  1开启")
  private String usdtCode;

  /** 银行卡开关 */
  @Schema(description = "usdt开关  0 关闭  1开启")
  private String bankCode;

  /** 银行卡开关 */
  @Schema(description = "银行卡解绑开关  0 关闭  1开启")
  private String bankSwith;

  /** usdt解绑开关 */
  @Schema(description = "usdt解绑开关  0 关闭  1开启")
  private String usdtSwith;
}

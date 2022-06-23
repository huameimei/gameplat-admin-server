package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberWithdrawVO {

  private Long id;

  private String cashOrderNo;

  private String account;

  private String realName;

  private BigDecimal accountMoney;

  private BigDecimal cashMoney;

  private String bankName;

  private String bankCard;

  private String bankAddress;

  private Integer cashStatus;

  private BigDecimal approveMoney;

  private String memberLevel;

  private String superName;

  private String ipAddress;

  private Integer proxyPayStatus;

  private String memberMemo;

  private String memberType;

  private String withdrawType;

  private BigDecimal currencyRate;

  private String currencyCount;

  private String approveCurrencyCount;

  private Date createTime;

  private BigDecimal counterFee;

  private String cashReason;

  private Long memberId;

  private String acceptAccount;

  private Date acceptTime;

  /**
   * 是否是银行卡黑名单(0-否 1-是)
   */
  private Integer isBankBlacklist;

  /**
   * 虚拟货币协议
   */
  private String currencyProtocol;
}

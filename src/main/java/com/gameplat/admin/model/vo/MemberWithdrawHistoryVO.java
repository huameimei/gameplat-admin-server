package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberWithdrawHistoryVO {

  private String cashOrderNo;

  private String account;

  private String realName;

  private BigDecimal accountMoney;

  private BigDecimal cashMoney;

  private String bankName;

  private String bankCard;

  private String bankAddress;

  private Integer cashMode;

  private Integer cashStatus;

  private String operatorAccount;

  private Date operatorTime;

  private String memberLevel;

  private String superName;

  private String ipAddress;

  private Integer proxyPayStatus;

  private String ppInterface;

  private String ppMerchantName;

  private String memberMemo;

  private String memberType;

  private String withdrawType;

  private BigDecimal currencyRate;

  private String currencyCount;

  private String approveCurrencyCount;

  private Date createTime;

  private BigDecimal counterFee;

  private BigDecimal approveMoney;

  private Long ppMerchantId;

  private String cashReason;

  private Long id;

  private Long memberId;

  private String acceptAccount;

  private Date acceptTime;
}

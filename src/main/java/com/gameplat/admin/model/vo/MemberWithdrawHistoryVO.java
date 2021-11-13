package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberWithdrawHistoryVO {

  private String cashOrderNo;

  private String account;

  private String nickname;

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

  private String withdrawType;

  private BigDecimal currencyRate;

  private String currencyCount;

  private String approveCurrencyCount;

  private Date createTime;

  private BigDecimal counterFee;

  private BigDecimal approveMoney;

}

package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MemberWithdrawHistoryQueryDTO implements Serializable {

  private Integer cashMode;

  private Integer cashStatus;

  private BigDecimal cashMoneyFrom;

  private BigDecimal cashMoneyFromTo;

  private Long ppMerchantId;

  private Integer proxyPayStatus;

  private String accounts;

  private String memberType;

  private List<String> memberLevelList;

  private String superName;

  private boolean allSubs;

  private List<String> bankNameList;

  private String bankCard;

  private String cashOrderNo;

  private String operatorAccounts;

  private String beginDatetime;

  private String endDatetime;

  private String order;

  private String orderBy;

  private Integer oprateMode;
}

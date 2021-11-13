package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UserWithdrawHistoryQueryDTO implements Serializable {

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

  private Date beginDatetime;

  private Date endDatetime;

  private String order;

  private String orderBy;

  private Integer oprateMode;

}

package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UserWithdrawQueryDTO implements Serializable {

  private BigDecimal cashMoneyFrom;

  private BigDecimal cashMoneyFromTo;

  private String superName;

  private List<String> memberLevelList;

  private List<String> bankNameList;

  private String bankCard;

  private List<String> cashStatusList;

  private String account;

  private String memberType;

  private String cashOrderNo;

  private String operatorAccount;

  private Date createTimeFrom;

  private Date createTimeTo;

  private String order;

  private List<Long> rechargeStatusList;

}

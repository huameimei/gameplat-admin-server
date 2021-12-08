package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class MemberWithdrawQueryDTO implements Serializable {

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

  private String createTimeFrom;

  private String createTimeTo;

  private String order;

  private List<Long> rechargeStatusList;

}

package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RechargeOrderQueryDTO implements Serializable {

  private String account;

  private String fullName;

  private List<String> memberLevelList;

  private String superAccount;

  private List<Integer> modeList;

  private String orderNo;

  private List<String> payAccountOwnerList;

  private Long tpMerchantId;

  private BigDecimal amountFrom;

  private BigDecimal amountTo;

  private Integer pointFlag;

  private List<Integer> statusList;

  private String createTimeFrom;

  private String createTimeTo;

  private String memberType;

  private boolean fullNameFuzzy;

  private String order;

}

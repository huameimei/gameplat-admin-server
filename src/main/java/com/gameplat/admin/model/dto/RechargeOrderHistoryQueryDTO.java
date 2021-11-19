package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class RechargeOrderHistoryQueryDTO implements Serializable {

  private List<Integer> discountTypes;

  private List<String> payTypeList;

  private String accounts;

  private List<String> memberLevelList;

  private String superAccount;

  private boolean superPathLike;

  private List<Integer> modeList;

  private String orderNo;

  private Long tpMerchantId;

  private BigDecimal amountFrom;

  private BigDecimal amountTo;

  private Integer pointFlag;

  private Integer status;

  private String beginDatetime;

  private String endDatetime;

  private String memberType;

  private String orderBy;

  private String order;

  private String auditorAccounts;

  private String remarks;

  private String auditRemarks;

  private List<String> payAccountOwnerList;

  private Date auditTime;

}

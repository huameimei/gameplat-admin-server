package com.gameplat.admin.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class RechargeOrderHistoryVO {

  public Long id;

  private String account;

  private String nickname;

  private BigDecimal balance;

  private String superAccount;

  private String orderNo;

  private String payTypeName;

  private String payAccountAccount;

  private String payAccountOwner;

  private String payAccountBankName;

  private String tpInterfaceName;

  private String tpMerchantName;

  private String tpPayChannelName;

  private String tpOrderNo;

  private BigDecimal amount;

  private BigDecimal discountAmount;

  private Integer discountType;

  private BigDecimal totalAmount;

  private BigDecimal discountDml;

  private String rechargePerson;

  private Date rechargeTime;

  private Integer status;

  private String remarks;

  private String auditorAccount;

  private Date auditTime;

  private String auditRemarks;

  private String ipAddress;

  private Date createTime;

  private String memberType;

  private Integer mode;

  private String payType;

  private String currencyCount;
}

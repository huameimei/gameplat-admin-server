package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class MemberBankVO implements Serializable {

  private Long id;

  private Long memberId;

  private String account;

  private String cardHolder;

  private String bankName;

  private String address;

  private String cardNo;

  private String network;

  private String alias;

  private String type;

  private String currency;

  private String isDefault;

  private String remark;

  private String createBy;

  private Date createTime;

  private String updateBy;

  private Date updateTime;
}

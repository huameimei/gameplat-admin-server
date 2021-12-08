package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class MemberBankVO implements Serializable {

  private Long id;

  private Long memberId;

  private String account;

  private String realName;

  private String bankName;

  private String subAddress;

  private String cardNo;

  private String province;

  private String city;

  private String type;

  private String currency;

  private String isDefault;

  private String remark;

  private String createBy;

  private Date createTime;

  private String updateBy;

  private Date updateTime;
}

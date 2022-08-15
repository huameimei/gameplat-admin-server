package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExternalDataVo implements Serializable {
  private String account;
  private String realname;
  private String balance;
  private String phone;
  private String bankCardNo;
  private String bankCardName;
  private String cardNo;
  private String network;
  private String remark;
  private String yx;
  private String wx;
  private String qq;
  private String birth;
  private String userLevelName;
  private String vipLevel;
  private String userType;
  private String dlLevel;
  private String parentName;
  private String superPath;
  private String registerIp;
  private String registerTime;
  private String lastLoginIp;
  private String lastLoginTime;
  private Integer rechCount;
  private String rechAmount;
  private Integer withCount;
  private String withAmount;
  //  private String firstRechAmount;
  //  private String firstWithAmount;
}

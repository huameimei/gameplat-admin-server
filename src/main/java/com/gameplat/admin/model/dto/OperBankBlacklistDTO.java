package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperBankBlacklistDTO implements Serializable {
  private int id;
  // 银行卡号
  private String cardNo;
  // 银行名称
  private String bankName;
  // 黑名单类型
  private String blackType;
  // 备注
  private String remarks;
}

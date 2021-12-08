package com.gameplat.admin.service.live.api.ae.bean;

import lombok.Data;

@Data
public class WithdrawReq extends AeRequest {

  private String userId;

  private String transferAmount;

  private String txCode;

  private String withdrawType;
}

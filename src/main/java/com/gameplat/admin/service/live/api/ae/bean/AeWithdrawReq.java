package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeWithdrawReq extends AeRequest {

  private String userId;

  private String transferAmount;

  private String txCode;

  private String withdrawType;
}

package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeGetBalanceReq extends AeRequest {

  private String userIds;

  private String isOffline;

  private String isFilterBalance;

  private String alluser;

}

package com.gameplat.admin.service.live.api.ae.bean;

import lombok.Data;

@Data
public class GetBalanceReq extends AeRequest {

  private String userIds;

  private String isOffline;

  private String isFilterBalance;

  private String alluser;

}

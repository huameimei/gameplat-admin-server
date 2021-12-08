package com.gameplat.admin.service.live.api.ae.bean;

import lombok.Data;

@Data
public class CreateMemberReq extends AeRequest {

  private String userId;

  private String language;

  private String currency;

  private String betLimit;
}

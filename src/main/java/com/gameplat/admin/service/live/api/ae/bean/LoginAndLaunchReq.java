package com.gameplat.admin.service.live.api.ae.bean;

import lombok.Data;

@Data
public class LoginAndLaunchReq extends AeRequest {

  private String userId;

  private String gameCode;

  private String gameType;

  private String platform;

  private String isMobileLogin;

  private String externalURL;

  private String language;

  private String hall;

}

package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeLoginAndLaunchReq extends AeRequest {

  private String userId;

  private String gameCode;

  private String gameType;

  private String platform;

  private String isMobileLogin;

  private String externalURL;

  private String language;

  private String hall;

}

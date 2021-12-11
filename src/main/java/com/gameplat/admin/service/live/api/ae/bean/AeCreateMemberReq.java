package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeCreateMemberReq extends AeRequest {

  private String userId;

  private String language;

  private String currency;

  private String betLimit;
}

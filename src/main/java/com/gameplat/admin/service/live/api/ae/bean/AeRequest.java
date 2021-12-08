package com.gameplat.admin.service.live.api.ae.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class AeRequest implements Serializable {

  private String cert;

  private String agentId;
}

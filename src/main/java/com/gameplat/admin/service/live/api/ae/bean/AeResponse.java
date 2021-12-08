package com.gameplat.admin.service.live.api.ae.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class AeResponse<T> implements Serializable {

  private String status;

  private String desc;
}

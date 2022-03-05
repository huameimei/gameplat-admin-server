package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayTypeQueryDTO implements Serializable {

  private Integer isSystemCode;

  private Integer status;

  private Integer onlinePayEnabled;
}

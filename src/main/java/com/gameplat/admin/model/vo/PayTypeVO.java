package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayTypeVO implements Serializable {

  private Long id;

  private String name;

  private String code;

  private String bankFlag;

  private String transferEnabled;

  private String iconUrl;

  private String onlinePayEnabled;

  private String status;
}

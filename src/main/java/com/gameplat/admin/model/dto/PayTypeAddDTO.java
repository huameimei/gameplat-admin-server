package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayTypeAddDTO implements Serializable {

  private String name;

  private String code;

  private String bankFlag;

  private Integer transferEnabled;

  private Integer onlinePayEnabled;

  private Integer isSysCode;

  private Integer sort;

  private String rechargeDesc;

  private String url;
}

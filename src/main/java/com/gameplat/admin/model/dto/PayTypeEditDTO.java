package com.gameplat.admin.model.dto;


import java.io.Serializable;
import lombok.Data;

@Data
public class PayTypeEditDTO implements Serializable {

  public Long id;

  private String name;

  private String code;

  private String bankFlag;

  private Integer transferEnabled;

  private Integer onlinePayEnabled;

  private String rechargeDesc;

  private Integer isSysCode;

  private Integer sort;
}

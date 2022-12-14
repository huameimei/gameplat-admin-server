package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameTransferRecordQueryDTO implements Serializable {

  private String account;

  /** 会员账号模糊匹配 */
  private Boolean accountFuzzy;

  private String orderNo;

  private String platformCode;

  private Integer transferType;

  private Integer status;

  private Integer transferStatus;

  private String createTimeStart;

  private String createTimeEnd;
}

package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class LiveTransferRecordQueryDTO implements Serializable {

  private String account;

  /**
   * 会员账号模糊匹配
   */
  private Boolean accountFuzzy;

  private String orderNo;

  private String liveCode;

  private Integer transferType;

  private Integer status;

  private Integer transferStatus;

  private String createTimeStart;

  private String createTimeEnd;
}

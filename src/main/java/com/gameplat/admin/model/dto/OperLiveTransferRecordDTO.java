package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class OperLiveTransferRecordDTO implements Serializable {

  private Long id;

  private String account;

  private Integer status;

  private String remark;

  private String liveCode;

  private Date updateTime;

  private String orderNo;

  private BigDecimal amount;
}

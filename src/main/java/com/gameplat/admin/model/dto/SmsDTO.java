package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SmsDTO implements Serializable {

  private String phone;
  private Integer status;
  private Integer smsType;
  private String validCode;
  /** 开始时间 */
  private String beginTime;

  /** 结束时间 */
  private String endTime;
}

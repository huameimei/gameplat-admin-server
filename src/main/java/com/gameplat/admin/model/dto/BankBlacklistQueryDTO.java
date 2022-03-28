package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class BankBlacklistQueryDTO implements Serializable {
  private String cardNo;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String startTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private String endTime;
}

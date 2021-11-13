package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BankBlacklistQueryDTO implements Serializable {
  private String cardNo;
}

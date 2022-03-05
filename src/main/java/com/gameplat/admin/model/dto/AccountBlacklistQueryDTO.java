package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountBlacklistQueryDTO implements Serializable {
  private String account;

  private String ip;
}

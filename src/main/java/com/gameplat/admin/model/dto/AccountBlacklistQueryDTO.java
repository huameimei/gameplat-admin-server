package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class AccountBlacklistQueryDTO implements Serializable {
  private String account;

  private String ip;
}

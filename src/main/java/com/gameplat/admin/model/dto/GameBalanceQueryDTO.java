package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameBalanceQueryDTO implements Serializable {
  private String platformCode;
  private String account;
}

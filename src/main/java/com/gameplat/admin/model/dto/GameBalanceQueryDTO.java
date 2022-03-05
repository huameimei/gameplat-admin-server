package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GameBalanceQueryDTO implements Serializable {
  private Map<String, String> platform;
  private String account;
}

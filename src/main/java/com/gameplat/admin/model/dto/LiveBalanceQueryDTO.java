package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class LiveBalanceQueryDTO implements Serializable {
  private Map<String, String> platform;
  private String account;
}

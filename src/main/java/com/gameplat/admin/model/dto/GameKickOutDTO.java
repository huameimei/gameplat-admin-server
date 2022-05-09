package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameKickOutDTO implements Serializable {
  private String platformCode;
  private String account;
  private String accounts;
}

package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperAccountBlacklistDTO implements Serializable {
  private Long id;

  private String account;

  private String ip;

  @NotNull
  private String games;

}

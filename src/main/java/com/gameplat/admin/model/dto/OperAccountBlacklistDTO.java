package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OperAccountBlacklistDTO implements Serializable {
  private Long id;

  private String account;

  private String ip;

  private String remark;

  @NotNull private String games;
}

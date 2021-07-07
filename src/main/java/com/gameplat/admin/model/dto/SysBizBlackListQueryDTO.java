package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysBizBlackListQueryDTO implements Serializable {
  private Integer userLevel;
  private String account;
  private Integer accountUserLevel;
  private Integer status;
}

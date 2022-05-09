package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameKickOutVO implements Serializable {

  private String account;

  private String platformCode;

  private BigDecimal balance;

  /** 0 : 成功 1 : 失败 */
  private Integer status;

  private String platformName;

  private String errorMsg;
}

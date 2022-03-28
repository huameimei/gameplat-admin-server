package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameBalanceVO implements Serializable {
  private String platformCode;

  private BigDecimal balance;

  /** 0 : 成功 1 : 失败 */
  private Integer status;

  private String platformName;

  private String errorMsg;
}

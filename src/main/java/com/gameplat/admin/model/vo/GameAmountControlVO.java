package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameAmountControlVO implements Serializable {

  /** '1.彩票 2.真人', */
  private Integer type;

  /** 额度 */
  private BigDecimal amount;

  /** 已用额度 */
  private BigDecimal useAmount;
}

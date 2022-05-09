package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameAmountNotifyVO implements Serializable {

  /** 额度 */
  private BigDecimal amount;

  /** 已输金额额度 */
  private BigDecimal useAmount;

  /** 已输金额占比 */
  private String percent;
}

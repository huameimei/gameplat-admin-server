package com.gameplat.admin.model.domain;

import com.gameplat.common.model.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 游戏额度控制
 */
@Data
public class GameAmountControl extends BaseEntity {

  /**
   * '1.彩票 2.真人',
   */
  private Integer type;

  /**
   * 额度
   */
  private BigDecimal amount;

  /**
   * 已用额度
   */
  private BigDecimal useAmount;

}

package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameConfiscatedVO implements Serializable {
  private String platformCode;

  /** 0 : 成功 1 : 失败 */
  private Integer status;

  private String platformName;

  private BigDecimal balance;

  private String errorMsg;
}

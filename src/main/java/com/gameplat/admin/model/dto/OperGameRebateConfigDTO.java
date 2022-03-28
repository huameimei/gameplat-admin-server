package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OperGameRebateConfigDTO implements Serializable {

  private Long id;
  /** 用户层级 */
  private String userLevel;

  /** 投注额阈值 */
  private BigDecimal money;

  /** 返点具体配置 */
  private String json;

  private String expand;
}

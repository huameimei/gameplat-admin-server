package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperGameTypeDTO implements Serializable {

  private Long id;

  /** 游戏大类编码 */
  private String gameTypeCode;

  /** 游戏大类名称 */
  private String gameTypeName;

  /** 状态 1.开启 0.关闭 */
  private Integer status;
}

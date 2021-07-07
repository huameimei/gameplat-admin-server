package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysBizBlacklistAddDTO implements Serializable {

  private Integer targetType;

  private String target;

  private String types;

  private String remark;

  private Integer status;

  /** 是否覆盖 */
  private boolean replaceExists;
}

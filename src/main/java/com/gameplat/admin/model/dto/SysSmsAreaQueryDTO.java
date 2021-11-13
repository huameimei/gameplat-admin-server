package com.gameplat.admin.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysSmsAreaQueryDTO implements Serializable {
  /**
   * 编码
   */
  private String code;
  /**
   * 国家/地区
   */
  private String name;

}

package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

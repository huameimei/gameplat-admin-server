package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysDictTypeEditDto extends BaseEntity {

  /**
   * 字典名称
   */
  private String dictName;

  /**
   * 字典类型
   */
  private String dictType;

  private String remark;

  private String dictSort;
}

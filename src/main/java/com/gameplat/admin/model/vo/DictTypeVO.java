package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典数据Vo
 *
 * @author three
 */
@Data
public class DictTypeVO implements Serializable {

  /** 字典主键 */
  private Long dictId;

  /** 字典名称 */
  private String dictName;

  /** 字典类型 */
  private String dictType;

  /** 状态（1正常 0停用） */
  private String status;

  /** 排序 */
  private Integer orderNum;
}

package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysDictTypeVO implements Serializable {

  private Long id;

  private String dictName;

  private String dictType;

  private String dictKind;

  private Integer status;

  private Integer dictSort;
}

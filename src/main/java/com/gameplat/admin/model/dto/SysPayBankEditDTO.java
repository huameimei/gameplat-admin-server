package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysPayBankEditDTO extends BaseEntity {

  private String bankName;

  private String bankCode;

  private Integer sort;

  private Integer status;

  private Integer bankType;

  private String img;
}

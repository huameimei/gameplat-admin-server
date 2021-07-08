package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysSmsQueryDTO implements Serializable {

  private String phone;

  private Integer smsType;

  private Integer status;
}

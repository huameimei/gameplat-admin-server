package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysSmsQueryDto implements Serializable {

  private String  phone;

  private Integer smsType;

  private Integer status;
}

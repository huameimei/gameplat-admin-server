package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PayAccountQueryDTO implements Serializable {

  private String account;

  private String payType;

  private List<String> memberLevelList;

  private Integer status;

  @ApiModelProperty(value = "1:普通账户，2：vpi账户")
  private Integer type;
}

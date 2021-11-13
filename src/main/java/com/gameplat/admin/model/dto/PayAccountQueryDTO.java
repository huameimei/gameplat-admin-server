package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PayAccountQueryDTO implements Serializable {


  private String account;

  private String payType;

  private List<String> userLevelList;

  private Integer status;
}

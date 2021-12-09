package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class PayAccountQueryDTO implements Serializable {


  private String account;

  private String payType;

  private List<String> memberLevelList;

  private Integer status;
}

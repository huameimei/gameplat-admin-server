package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class TpPayChannelQueryDTO implements Serializable {

  private Long tpInterfaceId;

  private Integer status;

  private String payType;

  private List<String> userLevelList;
}

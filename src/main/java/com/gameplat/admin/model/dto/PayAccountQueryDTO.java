package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import java.util.List;
import lombok.Data;

@Data
public class PayAccountQueryDTO extends BaseEntity {

  private String account;

  private String payType;

  private List<String> userLevelList;

  private Integer status;
}

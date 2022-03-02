package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Map;

@Data
public class LimitInfoDTO implements Serializable {

  @NotEmpty(message = "名称不能为空")
  private String name;

  @NotEmpty(message = "数据不能为空")
  private Map<String, Object> params;
}

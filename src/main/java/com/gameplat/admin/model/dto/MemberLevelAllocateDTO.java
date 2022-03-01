package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberLevelAllocateDTO implements Serializable {

  @NotNull(message = "层级编号不能为空")
  private Long id;

  @NotEmpty(message = "层级名称不能为空")
  private String levelName;

  @NotNull(message = "层级值不能为空")
  private Integer levelValue;
}

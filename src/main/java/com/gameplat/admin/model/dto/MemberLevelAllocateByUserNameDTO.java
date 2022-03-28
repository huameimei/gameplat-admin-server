package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberLevelAllocateByUserNameDTO implements Serializable {

  @NotNull(message = "会员账号不能为空")
  private String userNames;

  @NotNull(message = "层级值不能为空")
  private Integer levelValue;
}

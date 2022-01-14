package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberResetRealNameDTO implements Serializable {

  @NotNull(message = "会员编号不能为空")
  private Long id;

  @NotEmpty(message = "真实姓名不能为空")
  private String realName;
}

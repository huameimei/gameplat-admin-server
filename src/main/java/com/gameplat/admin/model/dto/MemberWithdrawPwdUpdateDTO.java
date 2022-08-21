package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MemberWithdrawPwdUpdateDTO implements Serializable {

  @NotNull(message = "会员编号不能为空")
  private Long id;

  private String password;
}

package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserResetPasswordDTO implements Serializable {

  @NotNull(message = "用户编号不能为空")
  private Long id;

  @NotNull(message = "新密码不能为空")
  private String password;
}

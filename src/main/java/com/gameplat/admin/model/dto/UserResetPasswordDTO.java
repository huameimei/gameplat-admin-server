package com.gameplat.admin.model.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserResetPasswordDTO implements Serializable {

  @NotNull(message = "用户编号不能为空")
  private Long id;

  @NotNull(message = "新密码不能为空")
  private String password;
}

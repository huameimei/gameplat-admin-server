package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperUserDTO implements Serializable {

  @NotNull(groups = Groups.UPDATE.class, message = "用户主键不能为空")
  private Long id;

  @NotEmpty(
      groups = {Groups.INSERT.class},
      message = "账号不能为空")
  private String account;

  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "用户昵称不能为空")
  private String nickName;

  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "用户类型不能为空")
  private String userType;

  private String avatar;

  private String phone;

  @NotEmpty(
      groups = {Groups.INSERT.class},
      message = "密码不能为空")
  private String password;

  private Integer status;

  private String safeCode;

  private Integer changFlag;

  private String setting;

  private String limitInfo;

  private Long roleId;
}

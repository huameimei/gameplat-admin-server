package com.gameplat.admin.model.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 管理员登录DTO
 *
 * @author three
 */
@Data
@Validated
public class AdminLoginDTO implements Serializable {

  /** 管理员账号 */
  @NotEmpty(message = "账号不能为空")
  private String account;

  /** 管理员密码 */
  @NotEmpty(message = "密码不能为空")
  private String password;

  /** 验证码 */
  private String valiCode;

  /** 谷歌动态码 */
  private String googleCode;

  private String userAgent;

  private String deviceId;
}

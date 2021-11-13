package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 谷歌验证DTO
 *
 * @author three
 */
@Data
public class GoogleAuthDTO {

  /** 账号 */
  @NotEmpty(message = "账号不能为空")
  private String loginName;

  /** 密钥 */
  @NotEmpty(message = "密钥不能为空")
  private String secret;

  /** 安全码 */
  @NotEmpty(message = "安全码不能为空")
  private String safeCode;
}

package com.gameplat.admin.model.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

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

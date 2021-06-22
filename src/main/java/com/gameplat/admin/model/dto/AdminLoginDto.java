package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class AdminLoginDto implements Serializable {

  /** 客户端ID */
  private String client_id;
  /** 客户端秘钥 */
  private String client_secret;
  /** 管理员账号 */
  private String account;
  /** 管理员密码 */
  private String password;
  /** 验证码 */
  private String valiCode;
  /** 谷歌动态码 */
  private String googleCode;
}

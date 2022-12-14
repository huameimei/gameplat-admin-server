package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToken implements Serializable {

  /** 用户名 */
  private String account;

  /** 昵称 */
  private String nickName;

  /** 用户token */
  private String accessToken;

  /** 用户token */
  private String refreshToken;

  /** token 过期时间 */
  private Long tokenExpireIn;

  /** 是否跳转到下载谷歌验证器页面 0 否 1 时 */
  private Integer changSafeCode;

  /** 客户端类型 */
  private String clientType;

  /** 用户设备 */
  private String deviceType;

  private String loginIp;

  private Date loginDate;

  /** 访问日志Token */
  private String accessLogToken;

  /** 是否通过了2FA认证 */
  private boolean authenticated;

  /** 是否启用了双因素认证 */
  private boolean isEnable2FA;
}

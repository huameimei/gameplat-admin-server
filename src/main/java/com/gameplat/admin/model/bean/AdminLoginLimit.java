package com.gameplat.admin.model.bean;

import lombok.Data;

@Data
public class AdminLoginLimit {

  /** 密码错误次数 */
  private Integer errorPwdLimit;
  /** 是否需要验证码 */
  private Integer vCode;
  /** 是否开启IP白名单 */
  private Integer openIpWhiteList;
  /** 开启谷歌验证码 */
  private Integer openGoogleAuth;
}

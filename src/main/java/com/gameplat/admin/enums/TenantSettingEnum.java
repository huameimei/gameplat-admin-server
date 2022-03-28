package com.gameplat.admin.enums;

public enum TenantSettingEnum {
  TENANT_SETTING_NAV("app_navigation", "导航栏"),
  TENANT_SETTING_CENTER("personal_center", "个人中心"),
  IS_INTO_BET("into_bet", "是否注单"),
  CAPTCHA_SWITCH("captcha_switch", "验证码开关"),
  START_UP_IMAGE("start_up_image", "启动图"),
  ;
  private String code;
  private String desc;

  TenantSettingEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}

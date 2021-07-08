package com.gameplat.admin.enums;

/** 用户受限类型 */
public enum UserLimitEnum {
  DEFAULT(0, "正常"),
  ERROR_PWD_NUM(1, "密码错误次数限制");
  private Integer value;
  private String desc;

  UserLimitEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }
}

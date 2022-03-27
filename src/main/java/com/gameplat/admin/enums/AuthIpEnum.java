package com.gameplat.admin.enums;

import java.util.Objects;

/** IP枚举 */
public class AuthIpEnum {

  public enum Type {

    /** 前台 */
    WEB("0", "前台"),

    /** 后台 */
    ADMIN("1", "后台");

    /** 值 */
    private final String value;

    /** 描述 */
    private final String desc;

    Type(String value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public String value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }

    public boolean match(String value) {
      return Objects.equals(this.value, value);
    }
  }
}

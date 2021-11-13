package com.gameplat.admin.enums;

public class SysUserEnums {

  public enum UserType {

    /** 管理员 */
    ADMIN("ADMIN"),

    /** 子账号 */
    SUB_USER("SUB_USER");

    private final String value;

    UserType(String value) {
      this.value = value;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

    public String value() {
      return value;
    }

    public static boolean isAdmin(String value) {
      return ADMIN.value.equals(value);
    }
  }
}

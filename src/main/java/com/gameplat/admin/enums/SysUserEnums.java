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

    public static boolean isAdmin(String value) {
      return ADMIN.value.equals(value);
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

    public String value() {
      return value;
    }
  }

  public enum Status {

    /** 禁用 */
    DISABLED(-1),

    /** 冻结 */
    FROZEN(0),

    /** 启用 */
    ENABLED(1);

    private final int value;

    Status(int value) {
      this.value = value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public int value() {
      return value;
    }
  }
}

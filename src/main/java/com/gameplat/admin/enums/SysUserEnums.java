package com.gameplat.admin.enums;

public class SysUserEnums {

  public enum UserType {

    /** 管理员 */
    ADMIN("ADMIN"),

    /** 子账号 */
    SUB_USER("SUB_USER"),

    /**充值会员、代理 */
    RECH_FORMAL_TYPE("M"),

    /**查询会员类型 */
    RECH_FORMAL_TYPE_QUERY("M,A"),

    /**充值推广 */
    RECH_TEST_TYPE("P"),

    /**提现会员、代理 */
    WITH_FORMAL_TYPE("HY"),

    /** 提现推广 */
    WITH_TEST_TYPE("VHY");



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

  public enum Status {

    /** 冻结 */
    FROZEN(-1),

    /** 禁用 */
    DISABLED(0),

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

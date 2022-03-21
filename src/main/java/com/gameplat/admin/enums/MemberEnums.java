package com.gameplat.admin.enums;

public class MemberEnums {

  public enum RegisterSource {

    /** 网页注册 */
    WEB(0),

    /** 苹果客户端 */
    IOS(1),

    /** 安卓客户端 */
    ANDROID(2),

    /** 后台添加 */
    BACKEND(3),

    /** H5注册 */
    WAP(4);

    private final int value;

    RegisterSource(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, RegisterSource registerSource) {
      return registerSource.value == value;
    }
  }

  public enum Status {

    /** 已禁用 */
    DISABLED(-1),

    /** 启用 */
    ENABlED(1),

    /** 冻结 */
    FROZEN(0);

    private final int value;

    Status(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, Status status) {
      return status.value == value;
    }
  }

  /** 会员类型 */
  public enum Type {

    /** 会员账号 */
    MEMBER("M", "会员账号"),

    /** 测试账号 */
    AGENT("A", "代理账号"),

    /** 测试账号 */
    TEST("T", "测试账号"),

    /** 推广账号 */
    PROMOTION("P", "推广账号");

    private final String value;

    private final String desc;

    Type(String value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public static boolean matchPromotion(String value) {
      return PROMOTION.value.equals(value);
    }

    public String value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

    public boolean match(String value, Type type) {
      return type.value.equals(value);
    }
  }
}

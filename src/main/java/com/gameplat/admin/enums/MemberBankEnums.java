package com.gameplat.admin.enums;

public class MemberBankEnums {

  /** 是否是默认 */
  public enum DEFAULT {

    /** 否 */
    N("N"),

    /** 是 */
    Y("Y");

    private final String value;

    DEFAULT(String value) {
      this.value = value;
    }

    public String value() {
      return this.value;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

    public boolean match(String value, DEFAULT def) {
      return def.value.equals(value);
    }
  }

  /** 类型 */
  public enum TYPE {

    /** 银行卡 */
    BANKCARD("B"),

    /** 虚拟币 */
    COIN("V");

    private final String value;

    TYPE(String value) {
      this.value = value;
    }

    public String value() {
      return this.value;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

    public boolean match(String value, TYPE type) {
      return type.value.equals(value);
    }
  }
}

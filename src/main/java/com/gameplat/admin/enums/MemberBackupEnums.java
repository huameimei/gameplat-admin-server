package com.gameplat.admin.enums;

public class MemberBackupEnums {

  public enum Type {

    /** 转代理数据 */
    AGENT(1),

    /** 会员层级数据 */
    LEVEL(2);

    private final int value;

    Type(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }
  }
}

package com.gameplat.admin.enums;

public enum LockedEnums {

  /** 未锁定 */
  UNLOCKED("N", "未锁定"),

  /** 锁定 */
  LOCKED("Y", "锁定");

  private final String value;

  private final String desc;

  LockedEnums(String value, String desc) {
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
    return this.value.equals(value);
  }
}

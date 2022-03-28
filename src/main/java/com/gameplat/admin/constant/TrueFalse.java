package com.gameplat.admin.constant;

public enum TrueFalse {

  /** 假 */
  FALSE(0),

  /** 真 */
  TRUE(1);

  private final int value;

  TrueFalse(int value) {
    this.value = value;
  }

  public static int valueOf(Boolean flag) {
    return Boolean.TRUE.equals(flag) ? TRUE.value : FALSE.value;
  }

  public int getValue() {
    return value;
  }

  public int valueOf(boolean flag) {
    return flag ? TRUE.value : FALSE.value;
  }
}

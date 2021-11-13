package com.gameplat.admin.enums;

public enum SwitchStatusEnum {
  ENABLED(0),

  DISABLED(1);

  private final int value;

  SwitchStatusEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public boolean match(int value) {
    return this.value == value;
  }
}

package com.gameplat.admin.enums;

public enum DefaultEnums {
  Y(1),

  N(0);

  private final int value;

  DefaultEnums(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public boolean match(int value) {
    return this.value == value;
  }
}

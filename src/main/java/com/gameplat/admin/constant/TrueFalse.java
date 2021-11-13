package com.gameplat.admin.constant;

public enum TrueFalse {
  FALSE(0), TRUE(1);

  private int value;

  TrueFalse(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

package com.gameplat.admin.enums;

public enum RechargeStatus {

  /** 未受理 */
  UNHANDLED(1),

  /** 已受理 */
  HANDLED(2),

  /** 已入款 */
  SUCCESS(3),

  /** 取消 */
  CANCELLED(4);

  private final int value;

  RechargeStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

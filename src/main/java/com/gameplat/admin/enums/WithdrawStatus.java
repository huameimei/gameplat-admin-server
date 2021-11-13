package com.gameplat.admin.enums;

public enum WithdrawStatus {

  /** 未受理 */
  UNHANDLED(1),
  /** 已受理 */
  HANDLED(2),
  /** 已出款 */
  SUCCESS(3),
  /** 已取消 */
  CANCELLED(4),
  /** 拒绝出款 */
  REFUSE(5);

  private int value;

  WithdrawStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

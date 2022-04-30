package com.gameplat.admin.enums;

public enum GameRebateReportStatus {

  /** 未发放 */
  UNACCEPTED(0),

  /** 已发放 */
  ACCEPTED(1),

  /** 已拒发 */
  REJECTED(2),

  /** 已回收 */
  ROLLBACKED(3);

  private final int value;

  GameRebateReportStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public boolean match(int value) {
    return this.value == value;
  }
}

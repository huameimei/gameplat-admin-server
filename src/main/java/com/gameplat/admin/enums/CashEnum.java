package com.gameplat.admin.enums;

/** 提现信息表的枚举 */
public enum CashEnum {

  /** 会员取款 */
  CASH_MODE_USER(1),
  /** 后台取款 */
  CASH_MODE_HAND(2),
  /** 第三方出款 */
  CASH_MODE_THIRD(3);

  private final int value;

  CashEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

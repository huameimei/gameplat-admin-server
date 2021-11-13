package com.gameplat.admin.enums;

/** 已受理订单是否允许其他用户操作枚举 */
public enum AllowOthersOperateEnums {

  /** 否 */
  NO(0),

  /** 是 */
  YES(1);

  private final int value;

  AllowOthersOperateEnums(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

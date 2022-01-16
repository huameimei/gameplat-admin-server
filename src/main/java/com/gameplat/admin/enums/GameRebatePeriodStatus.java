package com.gameplat.admin.enums;

public enum GameRebatePeriodStatus {
  UNSETTLED(0),// 未结算
  SETTLED(1), // 已结算
  ACCEPTED(2),//已派发
  ROLLBACKED(3); //已回收;

  private int value;

  GameRebatePeriodStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

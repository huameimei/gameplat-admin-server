package com.gameplat.admin.service.live.api.ae.enums;

/** 转出类别 */
public enum WithdrawType {

  /** 全部 */
  ALL("1"),

  /** 部分 */
  PARTIAL("0");

  private String value;

  WithdrawType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
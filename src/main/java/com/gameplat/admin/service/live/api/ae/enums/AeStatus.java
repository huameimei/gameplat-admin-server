package com.gameplat.admin.service.live.api.ae.enums;

/**
 * AE返回状态码
 */
public enum AeStatus {
  SYSTEM_BUSY("9998"),

  FAIL("9999"),

  SUCCESS("0000"),

  INVALID_USER_ID("1000"),

  ACCOUNT_EXISTED("1001"),

  ACCOUNT_NOT_EXISTS("1002"),

  TXCODE_ALREADY_OPERATION("1016"),

  TXCODE_NOT_EXIST("1017");

  private final String value;

  AeStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
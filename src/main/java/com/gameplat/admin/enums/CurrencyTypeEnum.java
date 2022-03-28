package com.gameplat.admin.enums;

/**
 * @author lily
 * @date 2021/11/27
 */
public enum CurrencyTypeEnum {

  /** 充值 */
  REAL(1, "真币"),

  /** 假币 */
  FALSE(2, "假币"),

  /** 代币 */
  TOKEN(3, "代币"),

  /** 冻结币 */
  FREEZE(4, "冻结币");

  private final int code;

  private final String detail;

  CurrencyTypeEnum(int code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  public int getCode() {
    return code;
  }

  public String getDetail() {
    return detail;
  }
}

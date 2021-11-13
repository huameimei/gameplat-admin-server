package com.gameplat.admin.enums;

import java.util.Arrays;

public enum ProxyPayStatusEnum {

  /** 代付返回成功 */
  PAY_SUCCESS(1, "success"),

  /** 代付进行中 */
  PAY_PROGRESS(0, "progress"),

  /** 代付失败 */
  PAY_FAIL(2, "fail");

  private final int code;

  private final String name;

  private ProxyPayStatusEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public static ProxyPayStatusEnum get(int code) {
    return Arrays.stream(ProxyPayStatusEnum.values())
        .filter(em -> (em.getCode() == code))
        .findFirst()
        .orElse(null);
  }

  public static String getName(int code) {
    return ProxyPayStatusEnum.get(code).getName();
  }
}

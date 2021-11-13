package com.gameplat.admin.enums;

import java.util.Arrays;

/** 后台出款模式 */
public enum OprateMode {

  OPRATE_MANUAL(1, "人工出款"),

  OPRATE_ATUO(2, "第三方出款"),

  OPRATE_VIRTUAL(3, "虚拟币出款"),

  OPRATE_MODE(4, "免提直充");

  private final Integer value;

  private final String desc;

  private OprateMode(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer value() {
    return value;
  }

  public String desc() {
    return desc;
  }

  public boolean match(Integer value) {
    return this.value.equals(value);
  }

  public static OprateMode get(Integer value) {
    return Arrays.stream(OprateMode.values())
        .filter(bl -> bl.value().equals(value))
        .findFirst()
        .orElse(null);
  }
}

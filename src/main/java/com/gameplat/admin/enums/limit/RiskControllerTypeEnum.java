package com.gameplat.admin.enums.limit;

import java.util.Arrays;

/** 风控金额设置 */
public enum RiskControllerTypeEnum {
  ANY_AMOUNT(0, "任意金额"),
  FLOAT_AMOUNT(1, "浮动金额"),
  FIXED_ACCOUNT(2, "固定金额"),
  FLOAT_FIXED_ACCOUNT(3, "固定金额");

  private Integer type;
  private String name;

  private RiskControllerTypeEnum(Integer type, String name) {
    this.type = type;
    this.name = name;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static RiskControllerTypeEnum get(Integer type) {
    return Arrays.stream(RiskControllerTypeEnum.values())
        .filter(em -> (em.getType() == type))
        .findFirst()
        .get();
  }

  public static String getName(int code) {
    return RiskControllerTypeEnum.get(code).getName();
  }
}

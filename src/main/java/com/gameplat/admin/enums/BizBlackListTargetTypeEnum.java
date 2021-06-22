package com.gameplat.admin.enums;

import java.util.Arrays;

/**
 * 黑名单对应范围
 * @author Lenovo
 */
public enum BizBlackListTargetTypeEnum {

  PERSON(0, "会员"),
  LEVEL(1, "层级");

  private Integer value;
  private String desc;

  BizBlackListTargetTypeEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static BizBlackListTargetTypeEnum get(int type) {
    return Arrays.stream(BizBlackListTargetTypeEnum.values()).filter(e -> e.getValue() == type).findFirst()
        .orElse(null);
  }

  public static String getDescInfo(int type, String defaultValue) {
    BizBlackListTargetTypeEnum states = BizBlackListTargetTypeEnum.get(type);
    return states != null ? states.getDesc() : defaultValue;
  }
}

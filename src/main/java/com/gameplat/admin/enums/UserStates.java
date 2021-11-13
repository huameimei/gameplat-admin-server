package com.gameplat.admin.enums;

import java.util.Arrays;

/** 用户状态 */
public enum UserStates {
  NO_AUDIT(0, "待审核"),
  DEFAULT(1, "正常"),
  DISABLE(2, "停用"),
  FROZEN(3, "冻结");

  private final Integer value;

  private final String desc;

  private UserStates(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static UserStates get(int type) {
    return Arrays.stream(UserStates.values())
        .filter(e -> e.getValue() == type)
        .findFirst()
        .orElse(null);
  }

  public static String getDescInfo(int type, String defaultValue) {
    UserStates states = UserStates.get(type);
    return states != null ? states.getDesc() : defaultValue;
  }
  /**
   * 用户是否可以登录
   *
   * @param value
   * @return
   */
  public static boolean isLogin(Integer value) {
    return UserStates.DEFAULT.getValue().equals(value);
  }

  /**
   * 用户是否可以进行充值和取现交易
   *
   * @param value
   * @return
   */
  public static boolean isMoneyDeal(Integer value) {
    return UserStates.DEFAULT.getValue().equals(value);
  }
}

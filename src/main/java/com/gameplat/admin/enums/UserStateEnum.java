package com.gameplat.admin.enums;

import java.util.Arrays;

/**
 * 管理员用户状态
 */
public enum UserStateEnum {
  DEFAULT(1, "正常"), DISABLE(2, "停用"), FROZEN(3, "冻结");
  private Integer value;
  private String desc;

  private UserStateEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static UserStateEnum get(int type) {
    return Arrays.stream(UserStateEnum.values()).filter(e->e.getValue()==type).findFirst().orElse(null);
  }

  public static String getDescInfo(int type, String defaultValue) {
    UserStateEnum states = UserStateEnum.get(type);
    return states != null ? states.getDesc() : defaultValue;
  }
  /**
   * 用户是否可以登录
   *
   * @param value
   * @return
   */
  public static boolean isLogin(Integer value) {
    return !UserStateEnum.DISABLE.getValue().equals(value);
  }

  /**
   * 用户是否可以进行充值和取现交易
   *
   * @param value
   * @return
   */
  public static boolean isMoneyDeal(Integer value) {
    return UserStateEnum.DEFAULT.getValue().equals(value);
  }
}

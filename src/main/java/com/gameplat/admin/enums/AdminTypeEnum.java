package com.gameplat.admin.enums;

import java.util.Arrays;

/**
 * 管理员类型枚举
 * @author Lenovo
 */
public enum AdminTypeEnum {

  SUPER(1, "超级管理员"),
  ADMIN(2, "管理员"),
  NORMAL(3, "子账号");
  private Integer value;
  private String desc;

  AdminTypeEnum(Integer value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public Integer getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }

  public static AdminTypeEnum get(int type) {
    return Arrays.stream(AdminTypeEnum.values()).filter(e -> e.getValue() == type).findFirst()
        .orElse(null);
  }

  public static String getDescInfo(int type, String defaultValue) {
    AdminTypeEnum states = AdminTypeEnum.get(type);
    return states != null ? states.getDesc() : defaultValue;
  }


  public static boolean isAdmin(Integer value) {
    return ADMIN.getValue().equals(value) || NORMAL.getValue().equals(value);
  }

  public static boolean isSuperAdmin(Integer value){
    return SUPER.getValue().equals(value);
  }

}

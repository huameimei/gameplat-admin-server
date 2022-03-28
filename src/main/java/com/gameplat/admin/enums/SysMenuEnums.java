package com.gameplat.admin.enums;

public class SysMenuEnums {

  public enum TYPE {

    /** 按钮 */
    BUTTON("F"),

    /** 菜单 */
    MENU("C"),

    /** 目录 */
    DICT("M");

    private final String value;

    TYPE(String value) {
      this.value = value;
    }

    public String value() {
      return this.value;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }
  }
}

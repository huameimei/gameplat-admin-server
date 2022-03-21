package com.gameplat.admin.enums;

/**
 * 用户层级相关枚举
 *
 * @author robben
 */
public class MemberLevelEnums {

  public enum Locked {

    /** 锁定 */
    Y(1),

    /** 未锁定 */
    N(0);

    private final int value;

    Locked(int value) {
      this.value = value;
    }

    public static boolean isLocked(int value) {
      return Y.value == value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, Locked locked) {
      return locked.value == value;
    }
  }

  public enum Default {

    /** 是 */
    Y(1),

    /** 否 */
    N(0);

    private final int value;

    Default(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, Default def) {
      return def.value == value;
    }
  }

  public enum Withdraw {

    /** 是 */
    Y(1),

    /** 否 */
    N(0);

    private final int value;

    Withdraw(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, Withdraw withdraw) {
      return withdraw.value == value;
    }
  }

  public enum Status {

    /** 启用 */
    Y(1),

    /** 禁用 */
    N(0);

    private final int value;

    Status(int value) {
      this.value = value;
    }

    public int value() {
      return this.value;
    }

    public boolean match(int value) {
      return this.value == value;
    }

    public boolean match(int value, Status status) {
      return status.value == value;
    }
  }
}

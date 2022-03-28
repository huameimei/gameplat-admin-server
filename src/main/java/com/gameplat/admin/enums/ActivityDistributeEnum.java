package com.gameplat.admin.enums;

/**
 * 活动分发状态
 *
 * @author kenvin
 */
public class ActivityDistributeEnum {

  public enum ActivityDistributeStatus {

    /** 无效 */
    INVALID(0, "无效"),

    /** 已结算 */
    SETTLED(2, "已结算"),

    /** 结算中 */
    SETTLEMENT(1, "结算中"),
    ;

    /** 值 */
    private final int value;

    /** 描述 */
    private final String desc;

    ActivityDistributeStatus(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc(int value) {
      for (ActivityDistributeStatus status : values()) {
        if (status.value == value) {
          return status.desc;
        }
      }
      return null;
    }
  }

  /** 活动领取方式 */
  public enum ActivityDistributeGetWayEnum {

    /** 直接领取 */
    DIRECT_RELEASE(1, "直接领取"),

    /** 福利中心 */
    WELFARE_CENTER(2, "福利中心"),
    ;

    /** 值 */
    private final int value;

    /** 描述 */
    private final String desc;

    ActivityDistributeGetWayEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc(int value) {
      for (ActivityDistributeGetWayEnum item : values()) {
        if (item.value == value) {
          return item.desc;
        }
      }
      return null;
    }
  }
}

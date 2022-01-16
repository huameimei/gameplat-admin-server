package com.gameplat.admin.enums;

/**
 * 活动信息枚举类
 *
 * @author kenvin
 */
public class ActivityInfoEnum {

  /** 统计项目 */
  public enum StatisItem {
    CUMULATIVE_RECHARGE_AMOUNT(1, "累计充值金额"),
    CUMULATIVE_RECHARGE_DAYS(2, "累计充值天数"),
    CONTINUOUS_RECHARGE_DAYS(3, "连续充值天数"),
    SINGLE_DAY_DEPOSIT_AMOUNT(4, "单日首充金额"),
    FIRST_DEPOSIT_AMOUNT(5, "首充金额"),
    CUMULATIVE_GAME_DML_AMOUNT(6, "累计游戏打码金额"),
    CUMULATIVE_GAME_DML_DAYS(7, "累计游戏打码天数"),
    CONSECUTIVE_GAME_DML_DAYS(8, "连续游戏打码天数"),
    SINGLE_DAY_GAME_LOSS(9, "单日游戏亏损金额"),
    CUMULATIVE_SPORTS_RECHARGE_AMOUNT(10, "指定比赛");

    private int value;
    private String desc;

    StatisItem(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc() {
      return this.desc;
    }
  }

  /** 活动类型 */
  public enum ActivityType {
    RECHARGE_ACTIVITY(1, "充值活动"),
    GAME_ACTIVITY(2, "游戏活动");

    private int value;
    private String desc;

    ActivityType(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc() {
      return this.desc;
    }
  }

  /** 统计日期类型 */
  public enum StatisDate {
    DAILY(1, "每日"),
    WEEKLY(2, "每周"),
    PER_MONTH(3, "每月"),
    SPECIFY_DAY_WEEK(4, "每周某天"),
    DAY_OF_THE_MONTH(5, "每月某日");

    private int value;
    private String desc;

    StatisDate(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return this.value;
    }

    public String getDesc() {
      return this.desc;
    }
  }

  /** 是否隔天申请 */
  public enum NextDayApply {
    YES(1, ""),
    NO(0, "");

    private int value;
    private String desc;

    NextDayApply(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 活动有效状态（1永久有效 2有时限） */
  public enum ValidStatus {
    PERMANENT(1, "永久有效"),
    TIME_LIMIT(2, "有时限");

    private int value;
    private String desc;

    ValidStatus(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 资格状态 */
  public enum QualificationStatusEnum {
    INVALID(0, "无效"),
    VALID(1, "有效"),
    AUDIT(2, "已审核"),
    ;

    QualificationStatusEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    private int value;
    private String desc;

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 黑名单限制类型 */
  public enum ActivityBlacklistEnum {
    MEMBER(1, "会员"),
    IP(2, "ip地址");

    /** 值 */
    private int value;

    /** 描述 */
    private String desc;

    ActivityBlacklistEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 活动申请方式 */
  public enum ApplyWayEnum {
    MANUAL(1, "手动申请"),
    AUTOMATIC(2, "自动申请");

    /** 值 */
    private int value;

    /** 描述 */
    private String desc;

    ApplyWayEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 活动审核方式 */
  public enum AuditWayEnum {
    MANUAL(1, "手动申请"),
    AUTOMATIC(2, "自动申请");

    /** 值 */
    private int value;

    /** 描述 */
    private String desc;

    AuditWayEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 活动大类 */
  public enum TypeEnum {
    RECHARGE(1, "充值活动"),
    RED_ENVELOPE(2, "红包活动");

    /** 值 */
    private int value;

    /** 描述 */
    private String desc;

    TypeEnum(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 福利领取方式 */
  public enum GetWay {
    DIRECT_RELEASE(1, "直接发放"),
    WELFARE_CENTER_PICK_UP(2, "福利中心领取");

    /** 值 */
    private int value;

    /** 描述 */
    private String desc;

    GetWay(int value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public int value() {
      return this.value;
    }

    public String desc() {
      return this.desc;
    }
  }

  /** 活动详细日期枚举 */
  public enum DetailDateEnum {
    MONDAY(1, "每周一"),
    TUESDAY(2, "每周二"),
    WEDNESDAY(3, "每周三"),
    THURSDAYS(4, "每周四"),
    FRIDAY(5, "每周五"),
    SATURDAY(6, "每周六"),
    SUNDAY(0, "每周日");

    private Integer value;
    private String week;

    DetailDateEnum(Integer value, String week) {
      this.value = value;
      this.week = week;
    }

    public static Integer getValue(String week) {
      DetailDateEnum[] detailDateEnums = values();
      for (DetailDateEnum detailDateEnum : detailDateEnums) {
        if (detailDateEnum.week().equals(week)) {
          return detailDateEnum.value();
        }
      }
      return null;
    }

    public static String getWeek(Integer value) {
      DetailDateEnum[] detailDateEnums = values();
      for (DetailDateEnum detailDateEnum : detailDateEnums) {
        if (detailDateEnum.value().equals(value)) {
          return detailDateEnum.week();
        }
      }
      return null;
    }

    private Integer value() {
      return this.value;
    }

    private String week() {
      return this.week;
    }
  }
}

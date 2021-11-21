package com.gameplat.admin.enums.activity;

/**
 * 活动详细日期枚举
 */
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

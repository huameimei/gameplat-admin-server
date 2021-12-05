package com.gameplat.admin.enums;

/**
 * 活动资格状态
 */
public enum ActivityQualificationStatusEnum {

    INVALID(0, "无效"),
    VALID(1, "有效"),
    AUDIT(2, "已审核"),
    ;

    /**
     * 值
     */
    private int value;

    /**
     * 描述
     */
    private String desc;

    ActivityQualificationStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}

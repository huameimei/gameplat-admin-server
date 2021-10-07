package com.gameplat.admin.enums;

import java.util.Arrays;

/**
 * 会员成长记录类型
 */
public enum GrowthRecordTypeEnum {
    RECHARGE(0, "充值"),
    SIGN(1, "签到"),
    DML(2, "打码量"),
    BACKEND(3, "后台修改"),
    PERFECT_INFO(4, "完善资料"),
    BIND_BANK(5, "绑定银行卡");
    private Integer value;
    private String desc;

    GrowthRecordTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static GrowthRecordTypeEnum get(int type) {
        return Arrays.stream(GrowthRecordTypeEnum.values()).filter(e -> e.getValue() == type).findFirst()
                .orElse(null);
    }

    public static String getDescInfo(int type, String defaultValue) {
        GrowthRecordTypeEnum states = GrowthRecordTypeEnum.get(type);
        return states != null ? states.getDesc() : defaultValue;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}

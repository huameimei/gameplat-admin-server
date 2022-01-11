package com.gameplat.admin.enums;

/**
 * 活动黑名单状态
 *
 * @author kenvin
 */
public enum ActivityBlacklistEnum {

    MEMBER(1, "会员"),
    IP(2, "ip地址");

    /**
     * 值
     */
    private int value;

    /**
     * 描述
     */
    private String desc;

    ActivityBlacklistEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }


}

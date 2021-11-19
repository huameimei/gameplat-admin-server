package com.gameplat.admin.enums;

/**
 * 消息状态
 */
public enum NoticeEnum {
    READ_STATUS_UNREAD(0, "消息未读"),
    READ_STATUS_READ(1, "消息已读"),
    ACCEPT_REMOVE_FLAG_YES(1, "接收者删除信息的标志,已删除"),
    ACCEPT_REMOVE_FLAG_NO(0, "接收者删除信息的标志,未删除"),
    SEND_REMOVE_FLAG_YES(1, "发送者删除信息的标志,已删除"),
    SEND_REMOVE_FLAG_NO(0, "发送者删除信息的标志,未删除"),
    IMMEDIATE_FLAG_YES(1, "是即时消息"),
    IMMEDIATE_FLAG_NO(0, "非即时消息"),
    ;

    private final Integer value;
    private final String desc;

    NoticeEnum(Integer value, String desc) {
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

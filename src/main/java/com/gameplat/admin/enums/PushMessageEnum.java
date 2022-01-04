package com.gameplat.admin.enums;

/**
 * 活动推送类型
 */
public class PushMessageEnum {

    public enum UserRange {
        ALL_MEMBERS(1, "全部会员"),
        SOME_MEMBERS(2, "部分会员"),
        ONLINE_MEMBER(3, "在线会员"),
        USER_LEVEL(4, "会员层级"),
        VIP_LEVEL(5, "VIP等级"),
        AGENT_LINE(6, "代理线");


        private int value;
        private String desc;

        UserRange(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static UserRange get(int value) {
            for (UserRange userRange : values()) {
                if (userRange.value == value) {
                    return userRange;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }


}

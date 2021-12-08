package com.gameplat.admin.enums;

/**
 * 活动推送类型
 */
public class PushMessageEnum {

    public enum UserRange {
        SOME_MEMBERS(1, "部分会员"),
        ALL_MEMBERS(2, "所有会有"),
        ONLINE_MEMBER(3, "在线会员"),
        SPECIFY_LEVEL(4, "指定层级"),
        AGENT_LINE(5, "代理线");


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

        public int getValue(){
            return this.value;
        }
    }


}

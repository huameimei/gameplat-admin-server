package com.gameplat.admin.enums;

/**
 * 活动推送类型
 */
public class PushMessageEnum {

    /** 推送范围 */
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

    /**弹出界面*/
    public enum Location {
        LOCATION_DEF(0, "默认"),
        RECOMMEND(1, "推荐"),
        HOME_LOBBY(2, "首页大厅"),
        LOTTERY_HOME(3, "彩票首页"),
        SPORTS_HOME(4, "体育首页"),
        GAME_HOME(5, "游戏首页");

        private int value;
        private String desc;

        Location(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static Location get(int value) {
            for (Location location : values()) {
                if (location.value == value) {
                    return location;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /** 弹出次数 */
    public enum PopCount {
        POP_COUNT_DEF(0, "默认"),
        ONLY_ONE(1, "只弹一次"),
        MANY_COUNT(2, "多次弹出");

        private int value;
        private String desc;

        PopCount(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static PopCount get(int value) {
            for (PopCount popCount : values()) {
                if (popCount.value == value) {
                    return popCount;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /** 消息类别 */
    public enum MessageCategory {

        CATE_DEF(0, "默认"),
        GAME(1, "游戏"),
        PORT(2, "体育"),
        LIVE(3, "直播"),
        SYS_SEND(4, "系统发送");

        private int value;
        private String desc;

        MessageCategory(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static MessageCategory get(int value) {
            for (MessageCategory messageCategory : values()) {
                if (messageCategory.value == value) {
                    return messageCategory;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /** 消息类别 */
    public enum MessageShowType {

        SHOW_DEF(0, "默认"),
        ROLL(1, "滚动"),
        TEXT_POPUP(2, "文本弹窗"),
        PIC_POPUP(3, "图片弹窗");

        private int value;
        private String desc;

        MessageShowType(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public static MessageShowType get(int value) {
            for (MessageShowType messageShowType : values()) {
                if (messageShowType.value == value) {
                    return messageShowType;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

}

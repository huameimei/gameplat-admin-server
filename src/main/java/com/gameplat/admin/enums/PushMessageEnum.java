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
        AGENT_LINE(6, "代理线"),
        NOTICE_MESSAGE(7, "通知消息");


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
        HOME_LOBBY(1, "首页大厅"),
        Lottery_HOME(2, "彩票首页"),
        SPORTS_HOME(3, "体育首页"),
        GAME_HOME(4, "游戏首页"),
        RECOMMEND(5, "推荐");

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
        ONLY_ONE(1, "只弹一次"),
        MANY_COUNT(2, "多次弹出"),
        ROLL(3, "滚动");

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
        TEXT_POPUP(1, "文本弹窗"),
        PIC_POPUP(2, "图片弹窗"),
        GAME(3, "游戏"),
        SPORT(4, "体育"),
        LIVE(5, "直播");

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

}

package com.gameplat.admin.enums;

/**
 * 活动大厅
 *
 * @author kenvin
 */
public class ActivityLobbyEnum {

    /**
     * 统计项目
     */
    public enum StatisItem {
        CUMULATIVE_RECHARGE_AMOUNT(1, "累计充值金额"),
        CUMULATIVE_RECHARGE_DAYS(2, "累计充值天数"),
        CONTINUOUS_RECHARGE_DAYS(3, "连续充值天数"),
        SINGLE_DAY_DEPOSIT_AMOUNT(4, "单日首充金额"),
        FIRST_DEPOSIT_AMOUNT(5, "首充金额"),
        CUMULATIVE_LOTTERY_RECHARGE_AMOUNT(6, "累计彩票打码金额"),
        ACCUMULATED_LOTTERY_CODING_DAYS(7, "累计彩票打码天数"),
        NUMBER_OF_CONSECUTIVE_LOTTERY_RECHARGE_DAYS(8, ""),
        SINGLE_DAY_LOTTERY_LOSS(9, "单日彩票亏损金额"),
        CUMULATIVE_SPORTS_RECHARGE_AMOUNT(10, "累计体育打码金额"),
        CONSECUTIVE_SPORTS_YARDING_DAYS(11, "连续体育打码天数"),
        AMOUNT_OF_SPORTS_LOSS_PER_DAY(12, "单日体育亏损金额");


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

    /**
     * 活动类型
     */
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

    /**
     * 统计日期类型
     */
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

    /**
     * 是否隔天申请
     */
    public enum NextDayApply {
        YES(1, ""),
        NO(0, "");

        private int value;
        private String desc;

        NextDayApply(int value, String desc) {
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


}
package com.gameplat.admin.enums;

/**
 * banner枚举
 *
 * @author admin
 */
public class SysBannerInfoEnum {

    /**
     * 状态
     */
    public enum Status {
        VALID(1, "有效"),
        INVALID(0, "无效"),
        ;

        private int value;
        private String desc;

        Status(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return this.value;
        }


    }

    /**
     * banner类型
     */
    public enum Type {
        SPORT(1, "体育"),
        LOTTERY(2, "彩票"),
        ;

        private int value;
        private String desc;

        Type(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return this.value;
        }


    }

}

package com.gameplat.admin.enums.activity;

/**
 * 游戏类型枚举
 */
public enum GameTypeEnum {

    SPORT(1, "体育"),
    REAL(2, "真人"),
    CHESS(3, "棋牌"),
    LOTTERY(4, "彩票"),
    EGAME(5, "电子"),
    ESPORT(6, "电竞"),
    ASPORT(7, "动物竞技"),
    HUNTER(8, "捕鱼");

    private Integer type;
    private String name;

    GameTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static Integer getType(String name) {
        GameTypeEnum[] gameTypeEnums = values();
        for (GameTypeEnum gameTypeEnum : gameTypeEnums) {
            if (gameTypeEnum.typeName().equals(name)) {
                return gameTypeEnum.gameType();
            }
        }
        return null;
    }

    public static String getName(Integer type) {
        GameTypeEnum[] gameTypeEnums = values();
        for (GameTypeEnum gameTypeEnum : gameTypeEnums) {
            if (gameTypeEnum.gameType().equals(type)) {
                return gameTypeEnum.typeName();
            }
        }
        return null;
    }

    private Integer gameType() {
        return this.type;
    }

    private String typeName() {
        return this.name;
    }

}

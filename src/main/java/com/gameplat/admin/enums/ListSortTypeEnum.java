package com.gameplat.admin.enums;

/**
 * @author james
 */

public enum ListSortTypeEnum {
    NAVIGATION("导航", "navigation"),
    BANNER("banner", "banner"),
    GAME_LIST("游戏列表", "gameList"),
    LOTT_LIST("彩系列表", "lotteryList"),
    WIN_LIST("中奖记录", "winList"),
    ;

    public String name;
    public String type;

    ListSortTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static String getTypeByName(String name) {
        for (ListSortTypeEnum value : ListSortTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value.type;
            }
        }
        return null;
    }

}

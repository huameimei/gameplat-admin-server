package com.gameplat.admin.enums;

/** 游戏类型枚举 */
public enum GameTypeEnum {

  /** 体育 */
  SPORT(1, "体育"),

  /** 真人 */
  REAL(2, "真人"),

  /** 棋牌 */
  CHESS(3, "棋牌"),

  /** 彩票 */
  LOTTERY(4, "彩票"),

  /** 电子 */
  EGAME(5, "电子"),

  /** 电竞 */
  ESPORT(6, "电竞"),

  /** 动物竞技 */
  ASPORT(7, "动物竞技"),

  /** 捕鱼 */
  HUNTER(8, "捕鱼");

  private final Integer type;
  private final String name;

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

  public static String getName(String type) {
    GameTypeEnum[] gameTypeEnums = values();
    for (GameTypeEnum gameTypeEnum : gameTypeEnums) {
      if (gameTypeEnum.gameType().toString().equals(type)) {
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

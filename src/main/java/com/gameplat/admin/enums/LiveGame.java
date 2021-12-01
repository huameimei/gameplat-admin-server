package com.gameplat.admin.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 额度转换类型
 */
public enum LiveGame {

  AG("ag", "AG", "美东"),
  BBIN("bbin", "BBIN", "美东");

  String code;
  String name;
  String betTimeDesc;

  LiveGame(String code, String name, String betTimeDesc) {
    this.code = code;
    this.name = name;
    this.betTimeDesc = betTimeDesc;
  }

  public static String getName(String code) {
    for (LiveGame game : LiveGame.values()) {
      if (game.getCode().equals(code)) {
        return game.getName();
      }
    }
    return "";
  }

  public static LiveGame findFirst(String code) {
    return Arrays.stream(LiveGame.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
  }

  public static List<LiveGame> getAll() {
    return Arrays.asList(LiveGame.values());
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getBetTimeDesc() {
    return betTimeDesc;
  }
}

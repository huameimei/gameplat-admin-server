package com.gameplat.admin.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

/** 会员黑名单部分 */
public enum UserBlackGames {
  cp(0, "彩票"),
  sport(1, "体育"),
  live(2, "全部真人"),
  chat(3, "聊天室"),
  activity(4, "优惠活动");

  private Integer value;
  private String text;

  UserBlackGames(Integer value, String text) {
    this.value = value;
    this.text = text;
  }

  public Integer getValue() {
    return value;
  }

  public String getText() {
    return text;
  }

  public static List<Map> getEnum() {
    TreeMap result = new TreeMap();
    Arrays.stream(values())
        .forEach(
            e -> {
              TreeMap map = new TreeMap();
              map.put("value", e.getValue());
              map.put("text", e.getText());
              result.put(e.getValue(), map);
            });
    return new ArrayList<>(result.values());
  }

  public static Optional<UserBlackGames> matches(Integer value) {
    return Optional.ofNullable(value)
        .flatMap(
            v -> Stream.of(UserBlackGames.values()).filter(e -> e.getValue().equals(v)).findAny());
  }
}

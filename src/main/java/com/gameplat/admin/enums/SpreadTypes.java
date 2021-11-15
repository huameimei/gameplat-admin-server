package com.gameplat.admin.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 推广类型
 *
 * @author three
 */
public enum SpreadTypes {

  /** 首页 */
  HOME(0, "首页"),
  /** 注册 */
  REGISTER(1, "注册"),
  ACTIVITY(2, "优惠活动"),
  SPORTS(3, "体育"),
  LIVE(4, "真人"),
  CHESS(5, "棋牌"),
  LOTTERY(6, "彩票"),
  EGAME(7, "电子"),
  ESPORT(8, "电竞"),
  FISHING(9, "捕鱼");

  private final Integer code;
  private final String value;

  SpreadTypes(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return this.code;
  }

  public String getValue() {
    return this.value;
  }

  public static SpreadTypes getValue(Integer code) {
    for (SpreadTypes types : values()) {
      if (types.getCode().equals(code)) {
        return types;
      }
    }
    return null;
  }

  public static List<Map<String, Object>> getAllList() {
    return Arrays.stream(values())
        .map(
            item -> {
              Map<String, Object> map = new HashMap<>();
              map.put("title", item.getValue());
              map.put("value", item.getCode());
              return map;
            })
        .collect(Collectors.toList());
  }
}

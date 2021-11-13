package com.gameplat.admin.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 密码错误次数限制方式
 *
 * @author three
 */
public enum ErrorPasswordLimit {

  /** 限制24小时 */
  DEFAULT(0, "24小时"),

  /** 需管理员解除限制 */
  ADMIN_RELIEVE(1, "需管理员解除限制");

  private final Integer key;

  private final String value;

  ErrorPasswordLimit(Integer key, String value) {
    this.key = key;
    this.value = value;
  }

  public Integer getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }

  public boolean match(Integer key) {
    return this.key.equals(key);
  }

  public static Optional<ErrorPasswordLimit> matches(String key) {
    return Optional.ofNullable(key)
        .flatMap(
            v ->
                Stream.of(ErrorPasswordLimit.values()).filter(e -> e.getKey().equals(v)).findAny());
  }
}

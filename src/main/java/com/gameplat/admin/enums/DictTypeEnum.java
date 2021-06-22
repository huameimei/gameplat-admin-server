package com.gameplat.admin.enums;

import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 * 常用配置枚举类
 * @author Lenovo
 */
public enum DictTypeEnum {

  ADMIN_LOGIN_CONFIG("ADMIN_LOGIN_CONFIG", "后台登录"),
  USER_LOGIN_LIMIT("USER_LOGIN_LIMIT", "会员登录"),
  USER_REGISTRY_CONFIG("USER_REGISTRY_CONFIG","会员注册配置"),
  USER_CASH_CONFIG("USER_CASH_CONFIG","会员提现配置"),
  GOOGLE_CONFIG("GOOGLE_CONFIG","谷歌验证器参数配置")
  ;
  private String value;
  private String text;

  DictTypeEnum(String value, String text) {
    this.value = value;
    this.text = text;
  }

  public String getValue() {
    return value;
  }

  public String getText() {
    return text;
  }

  public static Optional<DictTypeEnum> matches(String value) {
    return Optional.ofNullable(value)
        .map(
            v ->
                Stream.of(DictTypeEnum.values())
                    .filter(e -> StringUtils.equals(e.getValue(), v))
                    .findAny()
                    .orElse(null));
  }

}

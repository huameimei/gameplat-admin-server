package com.gameplat.admin.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 字典配置枚举类
 *
 * @author three
 */
public enum DictTypeEnum {

  /** 后台登录限制 */
  ADMIN_LOGIN_CONFIG("ADMIN_LOGIN_CONFIG", "后台登录"),
  /** 语言配置 */
  LANGUAGE_CONFIG("LANGUAGE_CONFIG", "语言配置"),
  /** 会员登录限制 */
  USER_LOGIN_LIMIT("USER_LOGIN_LIMIT", "会员登录"),
  /** 公共告警会员 */
  PUBLIC_WARNING_ACCOUNT("PUBLIC_WARNING_ACCOUNT", "公共告警会员"),
  /** 会员注册配置 */
  USER_REGISTRY_CONFIG("USER_REGISTRY_CONFIG", "会员注册配置"),
  /** 会员提现配置 */
  USER_CASH_CONFIG("USER_CASH_CONFIG", "会员提现配置"),
  /** 谷歌验证器参数配置 */
  GOOGLE_CONFIG("GOOGLE_CONFIG", "谷歌验证器参数配置"),
  /** 系统配置 */
  SYSTEM_CONFIG("SYSTEM_CONFIG", "系统配置"),
  /** 提款限额配置 */
  USER_WITHDRAW_LIMIT("USER_WITHDRAW_LIMIT", "提款限额配置"),
  /** 提款限额配置 */
  RECH_VIRTUAL("RECH_VIRTUAL", "虚拟货币");

  private final String key;

  private final String value;

  DictTypeEnum(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }

  public static Optional<DictTypeEnum> matches(String key) {
    return Optional.ofNullable(key)
        .flatMap(
            v ->
                Stream.of(DictTypeEnum.values())
                    .filter(e -> StringUtils.equals(e.getKey(), v))
                    .findAny());
  }
}

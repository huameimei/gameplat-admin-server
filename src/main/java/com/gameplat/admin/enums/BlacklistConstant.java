package com.gameplat.admin.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

public class BlacklistConstant {

  private BlacklistConstant() {}

  public enum BizBlacklistTargetType {
    USER(0, "会员"),
    USER_LEVEL(1, "层级");

    private final int value;
    private final String text;

    BizBlacklistTargetType(int value, String text) {
      this.value = value;
      this.text = text;
    }

    public int getValue() {
      return value;
    }

    public String getText() {
      return text;
    }

    public static Optional<BizBlacklistTargetType> matches(Integer value) {
      return Optional.ofNullable(value)
          .flatMap(
              v ->
                  Stream.of(BizBlacklistTargetType.values())
                      .filter(e -> e.getValue() == v)
                      .findAny());
    }
  }

  public enum BizBlacklistType {
    RECHARGE_DISCOUNT("RECHARGE_DISCOUNT", "充值优惠"),
    YU_BAO_INTEREST("YU_BAO_INTEREST", "余额宝收益"),
    LEVEL_WAGE_WEEK("LEVEL_WAGE_WEEK", "周俸禄"),
    LEVEL_WAGE_MONTH("LEVEL_WAGE_MONTH", "月俸禄"),
    RECHARGE_GROWTH("RECHARGE_GROWTH", "充值成长值"),
    LEVEL_UPGRADE_REWARD("LEVEL_UPGRADE_REWARD", "升级奖励"),
    DL_BONUS("DL_BONUS", "代理分红"),
    DL_DAY_WAGE("DL_DAY_WAGE", "日工资"),
    DL_RATIO("DL_RATIO", "层层代理分红"),
    LEVEL_LOAN("LEVEL_LOAN", "借呗借款"),
    SPORTS_REBATE("SPORTS_REBATE", "体育返水"),
    LOTTERY_REBATE("LOTTERY_REBATE", "彩票返水"),
    PROMOTION_BONUS("PROMOTION_BONUS", "活动彩金");
    private final String value;
    private final String text;

    BizBlacklistType(String value, String text) {
      this.value = value;
      this.text = text;
    }

    public String getValue() {
      return value;
    }

    public String getText() {
      return text;
    }

    public static Optional<BizBlacklistType> matches(String value) {
      return Optional.ofNullable(value)
          .map(
              v ->
                  Stream.of(BizBlacklistType.values())
                      .filter(e -> StringUtils.equals(e.getValue(), v))
                      .findAny()
                      .orElse(null));
    }
  }

  public enum BizBlacklistStatus {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");
    private int value;
    private String text;

    BizBlacklistStatus(int value, String text) {
      this.value = value;
      this.text = text;
    }

    public int getValue() {
      return value;
    }

    public String getText() {
      return text;
    }

    public static Optional<BizBlacklistStatus> matches(Integer value) {
      return Optional.ofNullable(value)
          .map(
              v ->
                  Stream.of(BizBlacklistStatus.values())
                      .filter(e -> e.getValue() == v)
                      .findAny()
                      .orElse(null));
    }
  }
}

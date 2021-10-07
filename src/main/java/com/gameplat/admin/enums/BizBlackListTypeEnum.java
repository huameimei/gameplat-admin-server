package com.gameplat.admin.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 黑名单类型
 *
 * @author Lenovo
 */
public enum BizBlackListTypeEnum {
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
    PROMOTION_BONUS("PROMOTION_BONUS", "活动彩金"),
    LIVE_PLAY_BLACK("LIVE_PLAY_BLACK", "真人游戏下注"),
    LOTTERY_BET_BLACK("LOTTERY_BET_BLACK", "彩票下注"),
    SPORT_BET_BLACK("SPORT_BET_BLACK", "体育游戏下注");
    private String value;
    private String text;

    BizBlackListTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public static Optional<BizBlackListTypeEnum> matches(String value) {
        return Optional.ofNullable(value)
                .map(
                        v ->
                                Stream.of(BizBlackListTypeEnum.values())
                                        .filter(e -> StringUtils.equals(e.getValue(), v))
                                        .findAny()
                                        .orElse(null));
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

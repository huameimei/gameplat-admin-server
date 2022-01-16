package com.gameplat.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: asky
 * @date: 2022/1/15 16:21
 * @desc:
 */
@AllArgsConstructor
public enum TimeTypeEnum {
    /**
     * 投注时间
     */
    BET_TIME(1),

    /**
     * 三方时间
     */
    THIRD_TIME(2),

    /**
     * 结算时间
     */
    SETTLE_TIME(3),

    /**
     * 报表统计时间
     */
    STAT_TIME(4);

    @Getter
    private final int value;
}

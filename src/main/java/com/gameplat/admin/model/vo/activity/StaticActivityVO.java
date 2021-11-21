package com.gameplat.admin.model.vo.activity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: whh
 * @Date: 2020/8/24 10:40
 * @Description: 统计用户 结算 vo
 */
@Data
public class StaticActivityVO {

    private Long userId;

    /**
     * 累计充值金额
     */
    private BigDecimal cumulativePayAmount;

    /**
     * 累计充值天数
     */
    private Integer cumulativePayDateCount;

    /**
     * 首充金额
     */
    private BigDecimal firstPayAmount;

    /**
     * 单日首充金额最大值
     */
    private BigDecimal firstPayDayAmount;

    /**
     * 支付时间
     */
    private Date payTime;


}

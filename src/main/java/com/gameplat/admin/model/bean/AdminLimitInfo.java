package com.gameplat.admin.model.bean;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 子账号限制信息
 * @author three
 */
@Data
public class AdminLimitInfo {

    /**
     * 单笔存款限额
     */
    private BigDecimal maxRechargeAmount;
    /**
     * 单笔取款限额
     */
    private BigDecimal maxWithdrawAmount;
    /**
     * 单笔人工存款限额
     */
    private BigDecimal maxManualRechargeAmount;
    /**
     * 单笔人工取款限额
     */
    private BigDecimal maxManualWithdrawAmount;
}

package com.gameplat.admin.model.bean;

import lombok.Data;

/**
 * 用户限制信息
 * @author three
 */
@Data
public class UserLimitInfo {

    /**
     * 单笔存款限额
     */
    private Integer maxRechargeAmount;
    /**
     * 单笔取款限额
     */
    private Integer maxWithdrawAmount;
    /**
     * 单笔人工存款限额
     */
    private Integer maxManualRechargeAmount;
    /**
     * 单笔人工取款限额
     */
    private Integer maxManualWithdrawAmount;
}

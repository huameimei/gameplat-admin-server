package com.gameplat.admin.model.doc;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushLotteryWin implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 不推送会员账号
     */
    private String blackAccounts;
    /**
     * 是否开启
     */
    private Integer isOpen;
    /**
     * 中奖最高前几名
     */
    private Integer topNum;
    /**
     * 最低中奖金额
     */
    private Double winMoney;

    /**
     *  房间进入特效
     */
    private String vipEnterLevels;
}


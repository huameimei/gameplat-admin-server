package com.gameplat.admin.model.vo;

import lombok.Data;

/**
 * 彩系彩种列表
 */
@Data
public class PushLottWinVo {

    private String account;

    /**
     * 彩种code
     */
    private String gameId;

    /**
     * 彩种名字
     */
    private String gameName;



    private double winMoney;


}
package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/3/2 14:55
 * @Version 1.0
 */
@Data
public class GameKGLotteryVo implements Serializable {

    /**
     * 彩种
     */
    private String gameName;


    /**
     * 投注人数
     */
    private int betNum;

    /**
     * 投注数
     */
    private int betOrderNum;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;


    /**
     * 有效投注金额
     */
    private BigDecimal vaildAmount;


    /**
     * 输赢金额
     */
    private BigDecimal winAmount;


    /**
     * 公司输赢
     */
    private BigDecimal companyWin;


    /**
     * 游戏输赢
     */
    private BigDecimal gameWin;
}

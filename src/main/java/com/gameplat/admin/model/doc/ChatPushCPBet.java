package com.gameplat.admin.model.doc;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChatPushCPBet implements Serializable {


     private static final long serialVersionUID = 1L;
    /**
     * 是否开启
     */
    private Integer isOpen;
    /**
     * 自动分享到聊天室
     */
    private int autoShare;

    /**
     *  直推会员
     */
    private String pushAccount;

    /**
     * 是否只直推会员
     */
    private Integer onlyPushAccount;

    /**
     *  推送黑名单
     */
    private String notPushAccount;

    /**
     * 推送会员需要的总充值金额
     */
    private BigDecimal rechMoney;

    /**
     * 推送会员需要的总充值次数
     */
    private Integer rechCount;

    /**
     * 推送会员需要的当日总充值金额
     */
    private BigDecimal todayRechMoney;

    /**
     * 玩法下注总金额
     */
    private Double totalMoney;

    /**
     * 是否显示跟投
     */
    private Integer showHeel;

    /**
     * 跟投显示最小金额
     */
    private double showHeelMinMoney;

    /**
     * 排行榜最高显示数
     */
    private Integer leaderBoardTotalCount;


    /**
     *  排行榜黑名单
     */
    private String leaderBoardBlackAccount;



    /**
     *  只推送哪些玩法
     */
    private String pushPlayCode;

    /**
     *  不推送哪些玩法
     */
    private String notPushPlayCode;

//    /**
//     *  游戏id
//     */
//    private String gameIds;

    /**
     *  房间进入特效
     */
    private String vipEnterLevels;
}


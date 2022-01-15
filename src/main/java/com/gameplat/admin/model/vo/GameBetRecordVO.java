package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class GameBetRecordVO implements Serializable {
    /**
     * 订单号
     */
    private String billNo;
    /**
     * 玩家账号
     */
    private String account;

    /**
     *  游戏大类
     */
    private String gameType;

    /**
     * 投注时间
     */
    private Date betTime;

    /**
     * 投注金额
     */
    private String betAmount;
    /**
     * 有效投注金额
     */
    private String validAmount;
    /**
     * 水钱
     */
    private String revenue;
    /**
     * 输赢金额
     */
    private String winAmount;
    /**
     * 游戏分类
     */
    private String gameKind;
    /**
     * 状态
     */
    private Integer settle;
    /**
     * 下注时间转北京时间
     */
    private Date gmtBetTime;
    /**
     * 报表统计时间
     */
    private String statTime;
    /**
     * 采集回来记录添加时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 美东时间
     */
    private Date amesTime;
    /**
     * 游戏名称
     */
    private String chineseName;

    /**
     * 投注内容
     */
    private String betContent;

    /**
     * 返水统计时间
     */
    private Date rebateTime;

    /**
     * 返水统计时间
     */
    private Date addTime;

    /**
     * 游戏房间号
     */
    private String room;

    /**
     * 游戏座位
     */
    private String chair;

    /**
     * 一级分类
     */
    private String firstKind;


}

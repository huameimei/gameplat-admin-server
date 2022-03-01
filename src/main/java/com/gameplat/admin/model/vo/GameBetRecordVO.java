package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.I18nSerializerUtils;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class GameBetRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单号
     */
    private String billNo;


    /**
     * 用户名
     */
    private String account;

    /**
     * 平台编码
     */
    private String platformCode;

    /**
     * 游戏分类
     */
    private String gameKind;

    /**
     * 一级分类
     */
    private String gameType;

    /**
     * 游戏编码
     */
    private String gameCode;

    /**
     * 游戏名称
     */
    @JsonSerialize(using = I18nSerializerUtils.class)
    private String gameName;

    /**
     * 币种
     */
    private String currency;

    /**
     * 结算状态
     */
    private Integer settle;

    /**
     * 投注金额
     */
    private String betAmount;

    /**
     * 有效投注额
     */
    private String validAmount;

    /**
     * 输赢金额
     */
    private String winAmount;

    /**
     * 投注内容
     */
    private String betContent;

    /**
     * 投注时间
     */
    private Date betTime;


    /**
     * 结算时间
     */
    private Date settleTime;

    /**
     * 报表统计时间
     */
    private Date statTime;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

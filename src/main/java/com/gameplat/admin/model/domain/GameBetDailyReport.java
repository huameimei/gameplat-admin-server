package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 游戏日报表
 */
@Data
@TableName("game_bet_daily_report")
public class GameBetDailyReport implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号编号
     */
    private Long memberId;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 上级ID
     */
    private Long superId;

    /**
     * 上级名称
     */
    private String superAccount;

    /**
     * 代理路径
     */
    private String userPaths;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 游戏平台
     */
    private String platformCode;

    /**
     * 游戏子类型
     */
    private String gameKind;

    /**
     * 游戏大类
     */
    private String gameType;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 有效投注额
     */
    private BigDecimal validAmount;

    /**
     * 中奖金额
     */
    private BigDecimal winAmount;


    /**
     * 下注笔数
     */
    private Long betCount;
    /**
     * 中奖数
     */
    private Long winCount;

    /**
     * 统计时间(年月日)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String statTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

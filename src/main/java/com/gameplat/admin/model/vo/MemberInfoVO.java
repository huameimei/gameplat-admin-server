package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MemberInfoVO implements Serializable {

    private Long id;

    /**
     * 会员账号
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String fullName;

    /**
     * 会员层级
     */
    private String userLevel;

    /**
     * 充值层级
     */
    private String rechLevel;

    /**
     * 账户余额 单位分
     */
    private Long balance;

    /**
     * 会员积分
     */
    private Long totalPoints;

    /**
     * 余宝金额
     */
    private Long yuBaoMoney;

    /**
     * 累计充值金额
     */
    private Long totalRechMoney;

    /**
     * 累计充值次数
     */
    private Integer totalRechTimes;

    /**
     * 累计出款金额
     */
    private Long totalWithdrawMoney;

    /**
     * 累计出款次数
     */
    private Integer totalWithdrawTimes;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 会员状态
     */
    private Integer status;

    /**
     * 是否在线
     */
    private Integer online;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;
    /**
     * 最近登录IP
     */
    private String lastLoginIp;

    /**
     * 当前登录时间
     */
    private Date loginTime;

    /**
     * 当前登录IP
     */
    private String loginIP;

    /**
     * 上级代理账号
     */
    private String agentAccount;
    // 成长等级
    private Integer growthLevel;
    // 成长值
    private Integer growthValue;
    /**
     * 在线情况
     */
    private Boolean isOnline;
}

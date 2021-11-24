package com.gameplat.admin.constant;

/**
 * 用户服 redisKey
 * @author lily
 */
public class MemberServiceKeyConstant {

    /**
     * 图片验证码key
     */
    public static final String V_CODE_KEY = "user:vCode:";


    /**
     * 极速验证key
     */
    public static final String GEETEST_CONFIG = "user:geetestConfig:";

    /**
     * 用户登录
     */
    public static final String LOGIN = "user:login:";

    /**
     * 账户资金锁 0 用户名
     */
    public static final String MEMBER_FINANCIAL_LOCK = "lock:member_financial:{0}";

    /**
     * 日结派发锁 0 租户标识
     */
    public static final String AGENCY_DAYDISTRIBUTEALL_LOCK="lock:agency_daydistributeall:{0}";


    // 开元棋牌登录、上分、下分并发锁
    public static final String KY_CHESS_LOCK = "lock:ky_chess:{0}:{1}";
}

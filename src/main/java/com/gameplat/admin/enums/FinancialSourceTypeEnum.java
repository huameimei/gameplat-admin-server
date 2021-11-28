package com.gameplat.admin.enums;

/**
*
* @author lily
* @date 2021/11/27
*/
public enum FinancialSourceTypeEnum {

    /** 全部 */
    ALL(0, "全部"),

	/** 充值 */
	CHARGE(1, "充值"),

	/** 提现 */
    WITHDRAW(2, "提现"),

	/** 转账 */
	TRANSFER(3, "转账"),

	/** 红利 */
    DIVIDEND(4, "红利"),

    /** 返水 */
    REBATE(5,"返水"),

    /** 提现退回 */
    WITHDRAWAL_RETURN(6,"提现退回"),

    /** 后台人工提现冻结 */
    BACK_CASH_FREEZE(7,"后台人工提现冻结"),

    /** 减币 */
    CUT_MONEY(8,"减币"),

    /** 代充 */
    SURROGATE(9,"代充"),

    /** 用户打赏 */
    USER_TO_LIVE(10, "用户打赏"),

    /** 加币 */
    ADD_MONEY(11,"加币"),

    /** 主播受赏 */
    LIVE_TO_USER(12, "主播受赏"),

    /** 插错账 */
    FAIR(13, "插错补单"),

    /** 游戏交易 */
    GAME_TRANSACTION(14,"游戏交易"),

    /** 游戏交易-转出 */
    GAME_TRANSACTION_OUT(15,"游戏交易-转出"),

    /** 游戏交易-转入 */
    GAME_TRANSACTION_IN(16,"游戏交易-转入"),

    /** 取消提现解冻 */
    CANCEL_CASH_THAW(17,"取消提现解冻"),

    /** 减币退回 */
    CUT_MONEY_RETURN(18,"减币退回"),

    /** 直播 */
    LIVE_BROADCAST(19,"直播"),

    /** 好友推荐 */
    RECOMMENDED_FRIENDS(20,"好友推荐"),

    /** 代币换真币 */
    TOKEN_TO_REAL(21,"代币换真币"),

    /** 真币换假币 */
    REAL_TO_FALSE(22,"真币换假币"),

    /** 申请提现时冻结 */
    APPLY_CASH_FREEZE(23,"申请提现时冻结"),

    /** 提现扣除 */
    CASH_DEDUCT(24,"提现扣除"),

    /** 提现没收 */
    CASH_AVAIL(25,"提现没收"),

    /** 真币清零 */
    REAL_CLEAR(26,"真币清零"),

    /** 假币清零 */
    FALSE_CLEAR(27,"假币清零"),

    /** 贷币清零 */
    LOAN_CLEAR(28,"贷币清零"),

    /** 冻结币清零 */
    FREEZE_CLEAR(29,"冻结币清零"),

    /** 注册赠送 */
    REGIS_GIVE(30,"注册赠送"),

    /** 微信赠送获得 */
    WE_REGIS_GIVE(31,"微信赠送获得"),

    /** 成长值升级奖励 */
    GROWTH_LEVEL_UP_REWORD(32,"升级奖励"),
    /** 周俸禄 */
    WEEK_WEAL(33,"周俸禄"),
    /** 月俸禄 */
    MONTH_WEAL(34,"月俸禄"),
    /** 生日礼金 */
    BIRTH_WEAL(35,"生日礼金"),
    /** 每月红包 */
    RED_ENVELOPE_WEAL(36,"每月红包"),

    /** 周俸禄回收 */
    WEEK_WEAL_RECYCLE(37,"周俸禄回收"),

    /** 月俸禄回收 */
    MONTH_WEAL_RECYCLE(38,"月俸禄回收"),
    /** 生日礼金回收 */
    BIRTH_WEAL_RECYCLE(39,"生日礼金回收"),
    /** 每月红包回收 */
    RED_ENVELOPE_WEAL_RECYCLE(40,"每月红包回收"),
    /** 额度转换 */
    LINE_CONVERSION(41,"额度转换"),
    /** 活动派发 */
    ACTIVITY_DISTRIBUTE(50,"活动派发");

    private int code;
    private String detail;

    FinancialSourceTypeEnum(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    //根据code获取详情
	public static String getDetailByCode(int code) {
		for (FinancialSourceTypeEnum financialSourceTypeEnum : values()) {
			if (financialSourceTypeEnum.getCode() == code) {
				return financialSourceTypeEnum.getDetail();
			}
		}
		return null;
	}
}

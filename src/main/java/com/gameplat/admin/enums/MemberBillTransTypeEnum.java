package com.gameplat.admin.enums;

/**
*
* @author lily
* @date 2021/11/27
*/
public enum MemberBillTransTypeEnum {

    /** 转账充值 */
    BANK_RECH(1, "转账充值"),

	/** 在线支付 */
    RECH(2, "在线支付"),

    /** 彩票下注 */
    FC_BET(3,"彩票下注"),

    /** 彩票派彩 */
    FC_BILL(4,"彩票派彩"),

    /** 人工提现 */
    TRANSFER_OUT(5,"人工提现"),

    /** 用户提款 */
    WITHDRAW(6, "用户提款"),

    /** 彩票撤单 */
    BET_CANCEL_BACK(7,"彩票撤单"),

    /** 提现退还 */
    WITHDRAW_FAIL(8, "提现退还"),

    /** 充值卡充值 */
    RECHARGE_CARD_RECH(9,"充值卡充值"),

    /** 返佣(分红) */
    BROKERAGE(10,"返佣(分红)"),

    /** 人工充值 */
    TRANSFER_IN(11, "人工充值"),

    /** 借呗放款 */
    LOAN_PAY(12,"借呗放款"),

    /** 借呗还款 */
    LOAN_REPAY(13,"借呗还款"),

    /** 活动彩金 */
    PROMOTION_BONUS(14,"活动彩金"),

    /** 绑定手机送彩 */
    BIND_PHONE(15,"绑定手机送彩"),

    /** 注册送彩金 */
    REGISTER(16, "注册送彩金"),

    /** 代理返点 */
    DL_REBATE(17,"代理返点"),

    /** 人工充值 */
    HAND_RECH(18, "人工充值"),

    /** 给予返水 */
    RENDER_REBATE(19, "给予返水"),

    /** 活动优惠 */
    ACTIVITY_DISCOUNT(20,"活动优惠"),

    /** 追号返款 */
    TRACE_NUM_BACK(21,"追号返款"),

    /** 系统奖励 */
    SYSTEM_REWARD(22,"系统奖励"),

    /** 代理日工资 */
    DL_DAY_RATE(23,"代理日工资"),

    /** 俸禄回收 */
    LEVEL_WAGE_ROLLBACK(24,"俸禄回收"),

    /** 其他充值 */
    OTHER_RECH(25,"其他充值"),

    /** 打和返款 */
    DRAW_BACK(26,"打和返款"),

    /** 冲销返水 */
    WRITE_OFF_REBATE(27,"冲销返水"),

    /** 人工提出 */
    HAND_WITHDRAW(28,"人工提出"),

    /** 追号扣款 */
    TRACE_NUM_DEDUCT(29,"追号扣款"),

    /** 优惠扣除 */
    DISCOUNT_DEDUCT(30,"优惠扣除"),

    /** 其他扣除 */
    OTHER_DEDUCT(31,"其他扣除"),

    /** 冲销派奖 */
    WRITE_OFF_PAYOUT(32,"冲销派奖"),

    /** 额度转入 */
    GAME_IN(33,"额度转入"),

    /** 额度转出 */
    GAME_OUT(34,"额度转出"),

    /** 体育下注 */
    SPORT_BET(35,"体育下注"),

    /** 体育结算 */
    SPORT_SETTLED(36,"体育结算"),

    /** 违规退还本金 */
    SPORT_BREAK_BACK(37,"违规退还本金"),

    /** 重新结算 */
    SPORT_RESETTLE(38,"重新结算"),

    /** 取消订单 */
    SPORT_CANCEL_ORDER(39,"取消订单"),

    /** 游戏返点 */
    GAME_REBATE(40,"游戏返点"),

    /** 游戏返点回收 */
    GAME_ROLLBACK(41,"游戏返点回收"),

    /** 棋牌支出 */
    LUCKY_OUT(42,"棋牌支出"),

    /** 棋牌收入 */
    LUCKY_IN(43,"棋牌收入"),

    /** 转出余额宝 */
    YUBAO_OUT(44,"转出余额宝"),

    /** 转入余额宝 */
    YUBAO_IN(45,"转入余额宝"),

    /** 升级奖励 */
    UPGRADE_REWARD(46,"升级奖励"),

    /** 周俸禄发放 */
    WEEK_WEAL(47,"周俸禄发放"),

    /** 月俸禄发放 */
    MONTH_WEAL(48, "月俸禄发放"),

    /** 生日礼金发放 */
    BIRTH_WEAL(49, "生日礼金发放"),

    /** 每月红包发放 */
    RED_ENVELOPE_WEAL(50, "每月红包发放"),

    /** 周俸禄回收 */
    WEEK_WEAL_RECYCLE(51,"周俸禄回收"),

    /** 月俸禄回收 */
    MONTH_WEAL_RECYCLE(52,"月俸禄回收"),

    /** 生日礼金回收 */
    BIRTH_WEAL_RECYCLE(53,"生日礼金回收"),

    /** 每月红包回收 */
    RED_ENVELOPE_WEAL_RECYCLE(54,"每月红包回收"),
    /** 层层代理分红金额 */
    DIVIDE_AMOUNT(55,"层层代分红"),
    /** 工资发放金额 */
    SALARY_AMOUNT(56,"代理工资"),
    /** 层层代理分红金额 */
    DIVIDE_AMOUNT_BACK(57,"层层代分红回收"),
    /** 工资发放金额 */
    SALARY_AMOUNT_BACK(58,"代理工资回收"),
    /** 平级分红 */
    SAME_DIVIDE_AMOUNT(59,"平级分红");
    ;










    private int code;
    private String detail;

    MemberBillTransTypeEnum(int code, String detail) {
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
		for (MemberBillTransTypeEnum financialSourceTypeEnum : values()) {
			if (financialSourceTypeEnum.getCode() == code) {
				return financialSourceTypeEnum.getDetail();
			}
		}
		return null;
	}
}

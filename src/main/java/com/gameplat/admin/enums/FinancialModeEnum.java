package com.gameplat.admin.enums;

/**
 * 交易方式
 *
 * @author admin
 */
public enum FinancialModeEnum {

    TRANSFER(1, "转账汇款"),
    ONLINE(2, "在线支付"),
    BACKSTAGE_DEPOSIT(3, "后台入款"),
    ;

    private int value;
    private String desc;

    FinancialModeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }


}

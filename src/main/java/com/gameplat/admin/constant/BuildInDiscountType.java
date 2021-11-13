package com.gameplat.admin.constant;


public enum BuildInDiscountType {

    PRE_VERSION(-1, -1),
    RECHARGE(TrueFalse.TRUE.getValue(), 0),
    FIRST_RECHARGE(TrueFalse.TRUE.getValue(), 1),
    REGISTER(TrueFalse.FALSE.getValue(), 2),
    BIND_PHONE(TrueFalse.FALSE.getValue(), 3),
    REDENVELOPE(TrueFalse.FALSE.getValue(), 20),
    RECHARGE_CARD(TrueFalse.TRUE.getValue(), 30);
    private int rechargeFlag;

    private int value;

    BuildInDiscountType(int rechargeFlag, int value) {
        this.rechargeFlag = rechargeFlag;
        this.value = value;
    }

    public int getRechargeFlag() {
        return rechargeFlag;
    }

    public int getValue() {
        return value;
    }
}

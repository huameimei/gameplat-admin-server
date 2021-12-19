package com.gameplat.admin.enums;

/**
 * @Description : 成长值变动类型
 * @Author : lily
 * @Date : 2021/12/08
 */
public enum GrowthChangeEnum {
    recharge(0, "充值"),
    sign(1, "签到"),
    dama(2, "打码量"),
    backEdit(3, "后台修改"),
    finishInfo(4, "完善资料"),
    bindBankCard(5, "绑定银行卡");

    private int code;
    private String detail;

    GrowthChangeEnum(int code, String detail) {
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
}

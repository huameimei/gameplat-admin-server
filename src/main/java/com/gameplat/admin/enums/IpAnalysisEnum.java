package com.gameplat.admin.enums;

/**
 * @author lily
 * @description ip分析枚举类型
 * @date 2022/1/19
 */
public enum IpAnalysisEnum {
  REGISTER(1, "注册"),
  LOGIN(2, "登录"),
  RECHARGE(3, "充值"),
  WITHDRAW(4, "提现"),
  ;

  private Integer code;
  private String message;

  IpAnalysisEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

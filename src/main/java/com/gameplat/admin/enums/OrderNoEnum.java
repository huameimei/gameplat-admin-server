package com.gameplat.admin.enums;

/**
 * @Author lily @Description 订单编号业务标识 @Date 2021/11/27
 */
public enum OrderNoEnum {

  /** 订单标识 */
  ORDER("O", "订单编号标识"),

  /** 支付标识 */
  PAY("P", "支付编号标识"),

  /** 退款标识 */
  REFUND("R", "退款编号标识"),

  /** 充值标识 */
  RECHARGE("RE", "充值编号标识"),

  /** 出款标识 */
  WITHDRAW("W", "出款编号标识"),

  FINANCIAL("F", "资金编号标识"),

  ACTIVITY_FINANCIAL("AF", "活动资金编号标识");

  private String code;
  private String message;

  OrderNoEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

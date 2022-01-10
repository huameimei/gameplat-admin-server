package com.gameplat.admin.constant;

import com.gameplat.common.enums.TranTypes;

import java.rmi.ServerException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum RechargeMode {
  TRANSFER(1, TranTypes.BANK_RECH.getValue(), "转账汇款"),
  ONLINE_PAY(2, TranTypes.RECH.getValue(), "在线支付"),
  MANUAL(3, TranTypes.TRANSFER_IN.getValue(), "后台人工转入");

  private int value;

  private int tranType;

  private String name;

  RechargeMode(int value, int tranType, String name) {
    this.value = value;
    this.tranType = tranType;
    this.name = name;
  }

  public static Integer getTranType(int value) {
    Optional<RechargeMode> optional = Stream.of(RechargeMode.values())
        .filter(rm -> rm.getValue() == value).findFirst();
    return optional.isPresent() ? optional.get().getTranType() : null;
  }

  public static RechargeMode get(int value) throws ServerException {
    return Arrays.stream(RechargeMode.values()).filter(e -> e.getValue() == value).findFirst()
        .orElseThrow(() -> new ServerException("错误的Recharge Mode:" + value));
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }

  public int getTranType() {
    return tranType;
  }

}

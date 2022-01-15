package com.gameplat.admin.enums;

import java.rmi.ServerException;
import java.util.Arrays;

public enum GameTransferStatus {

  // 平台转出成功真人转入失败
  OUT(1, "平台转出成功真人转入失败"),
  //真人转出成功平台转入失败
  IN(2, "真人转出成功平台转入失败"),
  //受理成功
  SUCCESS(3, "受理成功"),
  // 取消
  CANCEL(4, "取消"),
  // 真人转出失败
  IN_LIVE_FAIL(5, "真人转出失败"),
  ROLL_BACK(6, "真人转入失败，系统转出回滚");

  private Integer value;
  private String name;

  GameTransferStatus(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  public static GameTransferStatus get(int status) throws ServerException {
    return Arrays.stream(GameTransferStatus.values()).filter(e -> e.getValue().equals(status))
        .findFirst()
        .orElseThrow(() -> new ServerException("错误的状态:" + status));
  }

  public Integer getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
}

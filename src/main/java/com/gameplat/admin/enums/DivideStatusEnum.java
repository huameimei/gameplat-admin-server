package com.gameplat.admin.enums;

import java.rmi.ServerException;
import java.util.Arrays;

public enum DivideStatusEnum {

  // 期数的结算状态--未结算
  PERIODS_SETTLE_STATUS_UNSETTLE(1, "未结算"),
  // 期数的结算状态--已结算
  PERIODS_SETTLE_STATUS_SETTLEED(2, "已结算"),
  // 期数的派发状态--未派发
  PERIODS_GRANT_STATUS_UNGRANT(1, "未派发"),
  // 期数的派发状态--已派发
  PERIODS_GRANT_STATUS_GRANTED(2, "已派发"),
  // 期数的派发状态--已回收
  PERIODS_GRANT_STATUS_RECYCLE(3, "已回收"),
  // 汇总的状态--已结算
  SUMMARY_STATUS_SETTLEED(1, "已结算"),
  // 汇总的状态--已派发
  SUMMARY_STATUS_GRANTED(2, "已派发"),
  // 汇总的状态--部分派发
  SUMMARY_STATUS_SOME_GRANTED(3, "部分派发"),
  // 汇总的状态--已回收
  SUMMARY_STATUS_RECYCLE(4, "已回收"),
  // 工资期数状态--未结算
  SALARY_PERIODS_STATUS_UNSETTLE(1, "未结算"),
  // 工资期数状态--已结算
  SALARY_PERIODS_STATUS_SETTLED(2, "已结算"),
  // 工资期数状态--已派发
  SALARY_PERIODS_STATUS_GRANTED(3, "已派发"),
  // 工期期数状态--已回收
  SALARY_PERIODS_STATUS_RECYCLE(4, "已回收"),
  // 工资统计的达标状态--未达标
  SALARY_REACH_STATUS_UNREACH(1, "未达标"),
  // 工资统计的达标状态--已达标
  SALARY_REACH_STATUS_REACHED(2, "已达标"),
  // 工资统计的派发状态--未派发
  SALARY_GRANT_STATUS_UNGRANT(1, "未派发"),
  // 工资统计的派发状态--已派发
  SALARY_GRANT_STATUS_GRANTED(2, "已派发"),
  // 工资统计的派发状态--已回收
  SALARY_GRANT_STATUS_RECYCLE(3, "已回收"),
  ;

  public static final int DIVIDE_MODEL_FIX = 1;
  public static final int DIVIDE_MODEL_FISSION = 2;
  public static final int DIVIDE_MODEL_LAYER = 3;


  private int value;
  private String name;

  DivideStatusEnum(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public static DivideStatusEnum get(int status) throws ServerException {
    return Arrays.stream(DivideStatusEnum.values()).filter(e -> e.getValue() == status)
        .findFirst()
        .orElseThrow(() -> new ServerException("错误的状态:" + status));
  }

  public int getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
}

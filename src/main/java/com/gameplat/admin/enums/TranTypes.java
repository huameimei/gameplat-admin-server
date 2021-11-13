package com.gameplat.admin.enums;

import com.gameplat.admin.model.bean.TranTypeBean;
import lombok.extern.slf4j.Slf4j;

import java.rmi.ServerException;
import java.util.*;

/** 账变类型 */
@Slf4j
public enum TranTypes {

  // 收入
  BANK_RECH(1, "转账充值", 1), // 银行卡入账
  RECH(2, "线上支付", 1),
  TRANSFER_IN(11, "后台转入", 1),
  FC_BILL(4, "彩票派彩", 1), // 彩票结算
  BET_CANCEL_BACK(7, "撤单返款", 1), // 彩票撤单 体育撤单
  WITHDRAW_FAIL(8, "提款失败", 1), // 提现失败退回
  BROKERAGE(10, "退佣(分红)", 1), // 佣金
  REGISTER(16, "注册送彩金", 1),
  DL_REBATE(17, "代理返点", 1),
  HAND_RECH(18, "人工存入", 1), // 人工充值
  RENDER_REBATE(19, "给予返水", 1),
  ACTIVITY_DISCOUNT(20, "活动优惠", 1),
  RECHARGE_CARD_RECH(63, "充值卡充值", 1),
  TRACE_NUM_BACK(21, "追号返款", 1),
  SYSTEM_REWARD(22, "系统奖励", 1),
  DL_DAY_RATE(23, "代理日工资", 1),
  // DL_SUPPORT(24, "代理扶持", 1),
  OTHER_RECH(25, "其他充值", 1),
  DRAW_BACK(26, "打和返款", 1),
  LIVE_IN(33, "额度转入", 1),
  LIVE_REBATE(40, "真人返点", 1),
  SPORT_SETTLED(36, "体育结算", 1),
  SPORT_BREAK_BACK(37, "违规退还本金", 1),
  SPORT_CANCEL_ORDER(39, "取消订单", 1),
  ESPORT_SETTLED(43, "电竞王者荣耀结算", 1),
  ESPORT_CANCEL_ORDER(44, "电竞王者荣耀取消订单", 1),
  LUCKY_IN(51, "棋牌收入", 1),
  YUBAO_OUT(52, "转出余额宝", 1),
  UPGRADE_REWARD(54, "升级奖励", 1),
  LEVEL_WAGE(55, "俸禄发放", 1),
  LOAN_PAY(57, "借呗放款", 1),
  PROMOTION_BONUS(59, "活动彩金", 1),
  BIND_PHONE(60, "绑定手机送彩", 1),
  // 支出
  LUCKY_OUT(50, "棋牌支出", 2),
  FC_BET(3, "彩票下注", 2),
  WITHDRAW(6, "用户提款", 2),
  HAND_WITHDRAW(28, "人工提出", 2),
  TRANSFER_OUT(12, "后台转出", 2),
  WRITE_OFF_REBATE(27, "冲销返水", 2),
  TRACE_NUM_DEDUCT(29, "追号扣款", 2),
  DISCOUNT_DEDUCT(30, "优惠扣除", 2),
  OTHER_DEDUCT(31, "其他扣除", 2),
  WRITE_OFF_PAYOUT(32, "冲销派奖", 2),
  LIVE_OUT(34, "额度转出", 2),
  LIVE_ROLLBACK(41, "真人返点回收", 2),
  SPORT_BET(35, "体育下注", 2),
  SPORT_RESETTLE(38, "重新结算", 2),
  YUBAO_IN(53, "转入余额宝", 2),
  LEVEL_WAGE_ROLLBACK(56, "俸禄回收", 2),
  LOAN_REPAY(58, "借呗还款", 2);

  //  BANK_RECH(1, "银行卡入账", 1),//
  //  RECH(2, "第三方充值", 2), //
  //  FC_BET(3, "彩票下注", 3),//
  //  FC_BILL(4, "彩票结算", 3),//
  //  WITHDRAW(6, "用户提款",4), //
  //  BET_CANCEL_BACK(7, "彩票撤单", 3),//
  //  WITHDRAW_FAIL(8, "提现失败退回", 4),//
  //  BROKERAGE(10, "佣金", 8), //
  //  TRANSFER_IN(11, "后台转入", 5),
  //  TRANSFER_OUT(12, "后台转出",7),
  //  FT_BET(13, "体育下注", 6),
  //  FT_BILL(14, "体育结算", 6),
  //  FT_BET_CANCEL(15, "体育撤单", 6),
  //  REGISTER(16,"注册送彩",9),
  //  DL_REBATE(17, "代理返点", 6),//

  int type;
  String desc;
  int reportType;

  TranTypes(int type, String desc, int reportType) {
    this.type = type;
    this.desc = desc;
    this.reportType = reportType;
  }

  public int getValue() {
    return this.type;
  }

  public String getDesc() {
    return this.desc;
  }

  public int getReportType() {
    return reportType;
  }

  public static TranTypes get(int type) {
    for (TranTypes s : TranTypes.values()) {
      if (type == s.getValue()) {
        return s;
      }
    }
    return null;
  }

  public static List<Integer> getTranList(int reportType) {
    List<Integer> list = new ArrayList<>();
    for (TranTypes s : TranTypes.values()) {
      if (reportType == s.getReportType()) {
        list.add(s.getValue());
      }
    }
    return list;
  }

  public static List<TranTypeBean> getAllTranList() {
    List<TranTypeBean> list = new ArrayList<TranTypeBean>();
    for (TranTypes tran : TranTypes.values()) {
      list.add(new TranTypeBean(tran.getValue(), tran.getDesc(), tran.getReportType()));
    }
    return list;
  }

  public static Map<Integer, String> getTransMap() {
    Map<Integer, String> tranMap = new HashMap<Integer, String>();
    for (TranTypes tran : TranTypes.values()) {
      tranMap.put(tran.getValue(), tran.getDesc());
    }
    return tranMap;
  }

  @Override
  public String toString() {
    return this.getValue() + "-" + this.getDesc();
  }

  static {
    Set<Integer> types = new HashSet<>();
    for (TranTypes type : TranTypes.values()) {
      if (!types.add(type.getValue())) {
        String msg =
            String.format("账变类型TranTypes定义错误,value重复[%s:%s]", type.getValue(), type.getDesc());
        log.error(msg, new ServerException(msg));
      }
    }
  }
}

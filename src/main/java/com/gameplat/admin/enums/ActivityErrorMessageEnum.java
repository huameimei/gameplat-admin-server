package com.gameplat.admin.enums;

/** 活动详细日期枚举 */
public enum ActivityErrorMessageEnum {
  E001("E001", "用户ID不能为空"),

  E002("E002", "活动ID不能为空"),

  E003("E003", "该用户不存在"),

  E004("E004", "活动申请成功"),

  E005("E005", "活动申请失败"),

  E006("E006", "账号状态异常"),

  E007("E007", "活动信息异常"),

  E008("E008", "该活动已关闭"),

  E009("E009", "活动未开始"),

  E010("E010", "活动已结束"),

  E011("E011", "该活动不需要手动参与,次日将自动统计您是否符合该活动的参与条件"),

  E012("E012", "未到申请时间"),

  E013("E013", "您已被禁止参加此活动，如有疑问，请联系客服"),

  E014("E014", "活动的每个统计周期内,只能参与一次"),

  E015("E015", "该活动不可重复参加"),

  E016("E016", "你的条件不符合该活动要求"),

  E017("E017", "你的VIP等级不符合该活动要求"),

  E018("E018", "活动优惠列表信息异常"),

  E019("E019", "您在活动统计期间,没有有效的投注金额"),

  E020("E020", "您在活动统计期间没有有效的充值金额"),

  E021("E021", "您在活动统计期间的累计充值金额和打码量未达到活动最低标准"),

  E022("E022", "您在活动统计期间的累计充值天数和打码量未达到活动最低标准"),

  E023("E023", "您在活动统计期间的最大连续充值天数和打码量未达到活动最低标准"),

  E024("E024", "您在活动统计期间的单日首充金额和打码量未达到活动最低标准"),

  E025("E025", "您在活动统计期间的首充金额和打码量未达到活动最低标准"),

  E026("E026", "您在活动统计期间,对于指定的游戏,累计打码金额未达到活动最低标准"),

  E027("E027", "您在活动统计期间,对于指定的游戏,累计打码天数和总打码量未达到活动最低标准"),

  E028("E028", "您在活动统计期间,对于指定的游戏,最大连续打码天数和总打码量未达到活动最低标准"),

  E029("E029", "您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准"),

  E030("E030", "您在该场比赛的打码量未达到活动最低标准"),

  E031("E031", "该活动只针对新用户有效"),

  E032("E032", "您账号的第一笔首充订单并不在活动时间内"),

  E033("E033", "您在活动统计期间,对于指定的游戏,没有有效的投注金额"),

  E034("E034", "您在活动统计期间,对于指定的游戏,单日亏损金额和总打码量未达到活动最低标准"),

  E035("E035", "您没有该场比赛的投注记录"),

  E036("E036", "活动已经申请成功,请不要频繁点击参与活动按钮");

  private final String code;

  private final String message;

  ActivityErrorMessageEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static String geCode(String message) {
    ActivityErrorMessageEnum[] detailDateEnums = values();
    for (ActivityErrorMessageEnum detailDateEnum : detailDateEnums) {
      if (detailDateEnum.message().equals(message)) {
        return detailDateEnum.code();
      }
    }
    return message;
  }

  public static String getMessage(String code) {
    ActivityErrorMessageEnum[] detailDateEnums = values();
    for (ActivityErrorMessageEnum detailDateEnum : detailDateEnums) {
      if (detailDateEnum.code().equals(code)) {
        return detailDateEnum.message();
      }
    }
    return code;
  }

  private String code() {
    return this.code;
  }

  private String message() {
    return this.message;
  }
}

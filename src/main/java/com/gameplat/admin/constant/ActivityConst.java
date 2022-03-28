package com.gameplat.admin.constant;

/** 活动常量类 */
public class ActivityConst {

  /** 活动资格--0:无效 */
  public static int QUALIFICATION_STATUS_INVALID = 0;

  /** 活动资格--1:有效 */
  public static int QUALIFICATION_STATUS_VALID = 1;

  /** 活动资格--2:已审核 */
  public static int QUALIFICATION_STATUS_AUDIT = 2;

  /** 限制类型 1会员账号 */
  public static int BLACKLIST_LIMITED_TYPE_MEMBER = 1;

  /** 限制类型 2 ip地址 */
  public static int BLACKLIST_LIMITED_TYPE_IP = 2;

  /** 结算状态--无效状态 */
  public static int DISTRIBUTE_STATUS_INVALID = 0;

  /** 结算状态--已结算 */
  public static int DISTRIBUTE_STATUS_SETTLED = 2;

  /** 结算状态--已结算 */
  public static int DISTRIBUTE_STATUS_UNSETTLEMENT = 1;
}

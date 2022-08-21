package com.gameplat.admin.constant;

/**
 * 会员redisKey
 *
 * @author lily
 */
public interface MemberServiceKeyConstant {

  /** 账户资金锁 0 用户名 */
  String MEMBER_FINANCIAL_LOCK = "lock:member_financial:{0}";

  String MEMBER_TRANSFER_AGENT_LOCK = "lock:member_transferAgent:";
}

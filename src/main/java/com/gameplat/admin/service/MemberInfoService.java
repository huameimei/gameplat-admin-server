package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.CleanAccountDTO;
import com.gameplat.model.entity.member.MemberInfo;

import java.math.BigDecimal;

public interface MemberInfoService extends IService<MemberInfo> {

  /**
   * 提现更新余额<br>
   * 失败时，默认重试3次
   *
   * @param memberId Long
   * @param amount 金额，正数
   */
  void updateBalanceWithWithdraw(Long memberId, BigDecimal amount);

  /**
   * 充值更新余额<br>
   * 失败时，默认重试3次
   *
   * @param memberId Long
   * @param amount 金额，正数
   */
  void updateBalanceWithRecharge(Long memberId, BigDecimal amount, BigDecimal totalAmount, Integer pointFlag);

  /**
   * 更新余额<br>
   * 失败时，默认重试3次
   *
   * @param memberId 会员ID
   * @param amount 扣除传负数，增加传正数
   */
  void updateBalance(Long memberId, BigDecimal amount);

  /**
   * 获取用户彩票投注返点
   *
   * @param account String
   * @return BigDecimal
   */
  BigDecimal findUserRebate(String account);

  /**
   * 冻结金额退回<br>
   * 失败时，默认重试3次
   *
   * @param memberId Long
   * @param amount 金额，正数
   */
  void updateFreeze(Long memberId, BigDecimal amount);


  void updateUserWithTimes(Long memberId, BigDecimal amount, Integer pointFlag);

  /**
   * 获取用户下级最大投注返点
   *
   * @param agentAccount String
   * @return BigDecimal
   */
  BigDecimal findUserLowerMaxRebate(String agentAccount);

  int updateClearGTMember(CleanAccountDTO dto);
}

package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberInfo;

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
  void updateBalanceWithRecharge(Long memberId, BigDecimal amount);

  /**
   * 更新余额<br>
   * 失败时，默认重试3次
   *
   * @param memberId 会员ID
   * @param amount 扣除传负数，增加传正数
   */
  void updateBalance(Long memberId, BigDecimal amount);
}

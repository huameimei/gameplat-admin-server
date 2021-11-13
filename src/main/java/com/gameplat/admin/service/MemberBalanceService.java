package com.gameplat.admin.service;

import java.math.BigDecimal;

public interface MemberBalanceService {

  /**
   * 更新余额
   *
   * @param memberId 会员账号
   * @param updateAmount 更新的余额
   */
  void updateBalance(Long memberId, BigDecimal updateAmount);
}

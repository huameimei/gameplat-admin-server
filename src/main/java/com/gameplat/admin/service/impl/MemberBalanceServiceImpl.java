package com.gameplat.admin.service.impl;

import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.service.MemberBalanceService;
import com.gameplat.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBalanceServiceImpl implements MemberBalanceService {

  @Autowired private MemberInfoMapper memberInfoMapper;

  @Override
  @Retryable(label = "更新会员余额", backoff = @Backoff(delay = 1200, multiplier = 1.5))
  public void updateBalance(Long memberId, BigDecimal updateAmount) {
    MemberInfo memberInfo = memberInfoMapper.selectById(memberId);
    if (null == memberInfo) {
      throw new ServiceException("会员信息不存在!");
    }

    // 当前余额
    BigDecimal currentBalance = memberInfo.getBalance();
    // 更新后的余额
    BigDecimal newBalance = currentBalance.add(updateAmount);

    // 判断余额是否充足
    if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
      throw new ServiceException("账户余额不足!");
    }

    // 修改余额
    if (memberInfoMapper.updateBalance(memberId, currentBalance, newBalance) <= 0) {
      log.error("修改会员余额失败,id:{},修改余额：{}", memberId, updateAmount);
      throw new ServiceException("修改会员余额失败!");
    }
  }
}

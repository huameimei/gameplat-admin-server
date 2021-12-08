package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.domain.MemberInfo;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.base.common.exception.ServiceException;
import java.math.BigDecimal;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo>
    implements MemberInfoService {

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = 250L, multiplier = 1.5))
  public void updateBalanceWithWithdraw(Long memberId, BigDecimal amount) {
    if (BigDecimal.ZERO.compareTo(amount) >= 0) {
      throw new ServiceException("金额不正确，金额必须大于0");
    }

    MemberInfo memberInfo = this.getById(memberId);

    // 计算变更后余额
    BigDecimal newBalance = this.getNewBalance(memberInfo.getBalance(), amount.negate());

    MemberInfo entity =
        MemberInfo.builder()
            .memberId(memberId)
            .balance(newBalance)
            .lastWithdrawTime(new Date())
            .lastWithdrawAmount(amount)
            .totalWithdrawAmount(memberInfo.getLastWithdrawAmount().add(amount))
            .totalWithdrawTimes(memberInfo.getTotalWithdrawTimes() + 1)
            .version(memberInfo.getVersion())
            .build();

    // 如果是首次提现，则设置提现信息
    if (memberInfo.getTotalWithdrawTimes() == 0) {
      entity.setFirstWithdrawTime(new Date());
      entity.setFirstWithdrawAmount(amount);
    }

    if (!this.updateById(entity)) {
      log.error("提现更新会员信息失败：{}", entity);
      throw new ServiceException("提现更新会员信息失败！");
    }
  }

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = 300L, multiplier = 1.5))
  public void updateBalanceWithRecharge(Long memberId, BigDecimal amount) {
    if (BigDecimal.ZERO.compareTo(amount) >= 0) {
      throw new ServiceException("金额不正确，金额必须大于0");
    }

    MemberInfo memberInfo = this.getById(memberId);

    // 计算变更后余额
    BigDecimal newBalance = this.getNewBalance(memberInfo.getBalance(), amount);

    MemberInfo entity =
        MemberInfo.builder()
            .memberId(memberId)
            .balance(newBalance)
            .lastRechAmount(amount)
            .lastRechTime(new Date())
            .totalRechAmount(memberInfo.getTotalRechAmount().add(amount))
            .totalRechTimes(memberInfo.getTotalRechTimes() + 1)
            .version(memberInfo.getVersion())
            .build();

    // 如果是首次充值，则设置首冲信息
    if (memberInfo.getTotalRechTimes() == 0) {
      entity.setFirstRechTime(new Date());
      entity.setFirstRechAmount(amount);
    }

    if (!this.updateById(entity)) {
      log.error("充值更新会员信息失败：{}", entity);
      throw new ServiceException("充值更新会员信息失败！");
    }
  }

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = 500L, multiplier = 1.5))
  public void updateBalance(Long memberId, BigDecimal amount) {
    MemberInfo memberInfo = this.getById(memberId);

    // 计算变更后余额并判断余额是否充足
    BigDecimal currentBalance = memberInfo.getBalance();
    BigDecimal newBalance = this.getNewBalance(currentBalance, amount);

    if (!this.updateById(
        MemberInfo.builder()
            .memberId(memberId)
            .balance(newBalance)
            .version(memberInfo.getVersion())
            .build())) {
      log.error("更新会员:{}余额失败，当前余额：{}，更新金额：{}", memberId, currentBalance, amount);
      throw new ServiceException("更新会员余额失败!");
    }
  }

  private BigDecimal getNewBalance(BigDecimal current, BigDecimal amount) {
    BigDecimal newBalance = current.add(amount);
    if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
      log.error("会员余额不足，当前余额：{}，变更金额：{}", current, amount);
      throw new ServiceException("余额不足");
    }

    return newBalance;
  }
}

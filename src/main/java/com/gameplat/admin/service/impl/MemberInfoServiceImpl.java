package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.model.dto.CleanAccountDTO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.member.MemberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo>
    implements MemberInfoService {

  @Autowired(required = false)
  private MemberInfoMapper memberInfoMapper;

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
  public void updateBalanceWithRecharge(Long memberId, BigDecimal amount, BigDecimal totalAmount, Integer pointFlag) {
    if (BigDecimal.ZERO.compareTo(amount) > 0) {
      throw new ServiceException("金额不正确，金额必须大于等于0");
    }

    MemberInfo memberInfo = this.getById(memberId);

    // 计算变更后余额
    BigDecimal newBalance = this.getNewBalance(memberInfo.getBalance(), totalAmount);
    MemberInfo entity = null;
    //判断是否是计入积分
    if (pointFlag == 1) {
      //判断金额是否是充值金额
      Integer totalRechTimes = memberInfo.getTotalRechTimes();
      if (ObjectUtil.isNotEmpty(amount) && amount.compareTo(BigDecimal.ZERO) == 1) {
        totalRechTimes = memberInfo.getTotalRechTimes() + 1;
      }
      entity =
              MemberInfo.builder()
                      .memberId(memberId)
                      .balance(newBalance)
                      .lastRechAmount(amount)
                      .lastRechTime(new Date())
                      .totalRechAmount(memberInfo.getTotalRechAmount().add(amount))
                      .totalRechTimes(totalRechTimes)
                      .version(memberInfo.getVersion())
                      .build();

      // 如果是首次充值，则设置首冲信息
      if (memberInfo.getTotalRechTimes() == 0) {
        entity.setFirstRechTime(new Date());
        entity.setFirstRechAmount(amount);
      }
    } else {
      entity =
              MemberInfo.builder()
                      .memberId(memberId)
                      .balance(newBalance)
                      .version(memberInfo.getVersion())
                      .build();
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

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = 500L, multiplier = 1.5))
  public void updateFreeze(Long memberId, BigDecimal amount) {
    MemberInfo memberInfo = this.getById(memberId);

    // 计算变更后冻结余额并判断冻结余额是否充足
    BigDecimal currentFreeze = memberInfo.getFreeze();
    BigDecimal newFreeze = this.getNewBalance(currentFreeze, amount);

    if (!this.updateById(
        MemberInfo.builder()
            .memberId(memberId)
            .freeze(newFreeze)
            .build())) {
      log.error("更新会员:{}冻结余额失败，当前冻结余额：{}，更新冻结金额：{}", memberId, currentFreeze, amount);
      throw new ServiceException("更新会员冻结余额失败!");
    }
  }

  @Override
  @Retryable(value = Exception.class, backoff = @Backoff(delay = 500L, multiplier = 1.5))
  public void updateUserWithTimes(Long memberId, BigDecimal amount, Integer pointFlag) {
    MemberInfo memberInfo = this.getById(memberId);
    if (pointFlag == 1) {
      MemberInfo entity =
              MemberInfo.builder()
                      .memberId(memberId)
                      .lastWithdrawTime(new Date())
                      .lastWithdrawAmount(amount)
                      .totalWithdrawAmount(memberInfo.getTotalWithdrawAmount().add(amount))
                      .totalWithdrawTimes(memberInfo.getTotalWithdrawTimes() + 1)
                      .version(memberInfo.getVersion())
                      .build();

      // 如果是首次提现，则设置提现信息
      if (memberInfo.getTotalWithdrawTimes() == 0) {
        entity.setFirstWithdrawTime(new Date());
        entity.setFirstWithdrawAmount(amount);
      }
      this.updateById(entity);
    }
  }

  @Override
  public BigDecimal findUserRebate(String account) {
    return memberInfoMapper.findUserRebate(account);
  }

  @Override
  public BigDecimal findUserLowerMaxRebate(String agentAccount) {
    return memberInfoMapper.findUserLowerMaxRebate(agentAccount);
  }

  private BigDecimal getNewBalance(BigDecimal current, BigDecimal amount) {
    BigDecimal newBalance = current.add(amount);
    if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
      log.error("会员余额不足，当前余额：{}，变更金额：{}", current, amount);
      throw new ServiceException("余额不足");
    }

    return newBalance;
  }

  @Override
  public int updateClearGTMember(CleanAccountDTO dto) {
    return memberInfoMapper.updateClearGTMember(dto);
  }
}

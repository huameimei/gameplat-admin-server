package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.mapper.AgentDivideMapper;
import com.gameplat.admin.mapper.RebatePlanMapper;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.admin.service.AgentBaseService;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.snowflake.IdGeneratorSnowflake;
import com.gameplat.common.constant.NumberConstant;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.proxy.RebatePlan;
import com.gameplat.redis.redisson.DistributedLocker;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AgentBaseServiceImpl implements AgentBaseService {
  @Autowired private RebatePlanMapper rebatePlanMapper;

  @Autowired private AgentDivideMapper agentDivideMapper;

  @Autowired private DistributedLocker distributedLocker;

  @Autowired private MemberService memberService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberBillService memberBillService;

  /**
   * 校验方案是否存在
   *
   * @param planId
   * @return
   */
  public void rebatePlanCheck(Long planId) {
    List<RebatePlanVO> rebatePlanPOList =
        rebatePlanMapper.getRebatePlan(
            new RebatePlan() {
              {
                setPlanId(planId);
              }
            });
    if (rebatePlanPOList.size() != 1) {
      throw new ServiceException("方案不存在");
    }
  }

  /**
   * 校验方案是否在使用
   *
   * @param planId
   * @return
   */
  public void planUsedCheck(Long planId) {
    int result = agentDivideMapper.agentPlanCheck(1, planId);
    if (result > 0) {
      throw new ServiceException("方案正在被使用");
    }
  }

  /**
   * 佣金结算
   *
   * @param agentId
   * @param settleMoney
   */
  @Override
  public void settle(
      String countDate, String operateBy, Long agentId, String agentName, BigDecimal settleMoney) {
    // 校验会员账户状态
    Member member = memberService.getById(agentId);
    if (BeanUtil.isEmpty(member)) {
      throw new ServiceException("代理不存在");
    }
    MemberInfo memberInfo =
        memberInfoService.lambdaQuery().eq(MemberInfo::getMemberId, agentId).one();
    if (BeanUtil.isEmpty(memberInfo)) {
      throw new ServiceException("代理信息不存在");
    }

    // 账户资金锁
    String lockKey =
        MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, agentName);

    try {
      // 获取资金锁（等待8秒，租期120秒）
      boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
      if (!flag) {
        return;
      }
      // 添加流水记录
      MemberBill memberBill = new MemberBill();
      memberBill.setMemberId(agentId);
      memberBill.setAccount(agentName);
      memberBill.setMemberPath(member.getSuperPath());
      memberBill.setTranType(TranTypes.SAME_REPORT_AMOUNT.getValue());
      memberBill.setOrderNo(String.valueOf(IdGeneratorSnowflake.getInstance().nextId()));
      memberBill.setAmount(settleMoney);
      memberBill.setBalance(memberInfo.getBalance());
      String sb = "期号：".concat(countDate).concat("，代理账号：").concat(agentName).concat("，平级方案分红");
      memberBill.setRemark(sb);
      memberBill.setContent(sb);
      UserCredential userCredential = SecurityUserHolder.getCredential();
      memberBill.setOperator(userCredential.getUsername());
      memberBillService.save(memberBill);
    } catch (Exception e) {
      log.error(MessageFormat.format("代理账号：{}，平级方案分红, 失败原因：{}", member.getAccount(), e));
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    } finally {
      // 释放资金锁
      distributedLocker.unlock(lockKey);
    }

    // 计算变更后余额
    BigDecimal newBalance = memberInfo.getBalance().add(settleMoney);
    if (newBalance.compareTo(BigDecimal.ZERO) < NumberConstant.ZERO) {
      return;
    }
    MemberInfo entity =
        MemberInfo.builder()
            .memberId(member.getId())
            .balance(newBalance)
            .version(memberInfo.getVersion())
            .build();
    boolean b = memberInfoService.updateById(entity);
    if (!b) {
      log.error("代理账号{}平级方案分红钱包余额变更失败！", member.getAccount());
    }
  }
}

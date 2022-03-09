package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberRwReportMapper;
import com.gameplat.admin.service.MemberRwReportService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.common.enums.CashEnum;
import com.gameplat.common.enums.RechargeMode;
import com.gameplat.common.enums.TrueFalse;
import com.gameplat.common.util.UserDlUtil;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberRwReport;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.recharge.RechargeOrder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberRwReportServiceImpl extends ServiceImpl<MemberRwReportMapper, MemberRwReport>
    implements MemberRwReportService {

  @Autowired
  private MemberRwReportMapper memberRwReportMapper;

  @Override
  public void addRecharge(Member member, Integer rechargeCount, RechargeOrder rechargeOrder)
      throws Exception {
    MemberRwReport report = getOrCreateReportUserRw(member, rechargeOrder.getAuditTime());
    BigDecimal amount = rechargeOrder.getPayAmount();
    if (rechargeOrder.getPointFlag() == TrueFalse.TRUE.getValue()
        && BigDecimal.ZERO.compareTo(amount) < 0) {
      // 计算积分且充值金额大于 0 累加充值次数
      if (rechargeOrder.getMode() == RechargeMode.TRANSFER.getValue()) {
        if (null != rechargeOrder.getCurrencyCount()) {
          report.setVirtualRechargeMoney(amount);
          report.setVirtualRechargeNumber(rechargeOrder.getCurrencyCount());
        }
        report.setBankCount(report.getBankCount() + 1);
        report.setBankMoney(report.getBankMoney().add(amount));
      } else if (rechargeOrder.getMode() == RechargeMode.ONLINE_PAY.getValue()) {
        report.setOnlineCount(report.getOnlineCount() + 1);
        report.setOnlineMoney(report.getOnlineMoney().add(amount));
      } else if (rechargeOrder.getMode() == RechargeMode.MANUAL.getValue()) {
        report.setHandRechCount(report.getHandRechCount() + 1);
        report.setHandRechMoney(report.getHandRechMoney().add(amount));
      }
      // 计算首充
      if (rechargeCount == 0) {
        report.setFirstRechType(rechargeOrder.getMode());
        report.setFirstRechMoney(report.getFirstRechMoney().add(amount));
      }
    } else if (rechargeOrder.getPointFlag() == TrueFalse.FALSE.getValue()) {
      // 计算异常金额
      report.setExceptionRechargeAmount(
          report.getExceptionRechargeAmount().add(rechargeOrder.getAmount()));
    }

    // 计算优惠
    if (rechargeOrder.getDiscountRechargeFlag() != null
        && rechargeOrder.getDiscountRechargeFlag() == TrueFalse.TRUE.getValue()) {
      report.setRechDiscount(report.getRechDiscount().add(rechargeOrder.getDiscountAmount()));
    } else {
      report.setOtherDiscount(report.getOtherDiscount().add(rechargeOrder.getDiscountAmount()));
    }
    report.setMemberId(member.getId());
    this.saveOrUpdate(report);
  }

  @Override
  public void addWithdraw(Member member, Integer withdrawCount, MemberWithdraw memberWithdraw)
      throws Exception {
    MemberRwReport report = getOrCreateReportUserRw(member, memberWithdraw.getOperatorTime());
    // 计算异常金额
    if (memberWithdraw.getPointFlag() == TrueFalse.FALSE.getValue()) {
      report.setExceptionWithdrawAmount(report.getExceptionWithdrawAmount().add(memberWithdraw.getCashMoney()));
    } else {
      if (memberWithdraw.getCashMode() == CashEnum.CASH_MODE_HAND.getValue()) {
        report.setHandWithdrawCount(report.getHandWithdrawCount() + 1);
        report.setHandWithdrawMoney(report.getHandWithdrawMoney().add(memberWithdraw.getCashMoney()));
      } else if (memberWithdraw.getCashMode() == CashEnum.CASH_MODE_USER.getValue()) {
        report.setWithdrawCount(report.getWithdrawCount() + 1);
        report.setWithdrawMoney(report.getWithdrawMoney().add(memberWithdraw.getCashMoney()));
      }
      // 计算首提
      if (memberWithdraw.getPointFlag() == TrueFalse.TRUE.getValue() && withdrawCount == 0) {
        report.setFirstWithdrawType(memberWithdraw.getCashMode());
        report.setFirstWithdrawMoney(report.getFirstWithdrawMoney().add(memberWithdraw.getCashMoney()));
      }
    }
    this.saveOrUpdate(report);
  }

  private MemberRwReport getOrCreateReportUserRw(Member member, Date date) throws Exception {
    MemberRwReport report = this.queryByMemberAndDate(member.getId(), date);
    // 新建记录 初始化
    if (null == report.getId()) {
      report.setMemberId(member.getId());
      report.setAccount(member.getAccount());
      report.setMemberId(member.getId());
      report.setParentAccount(member.getParentName());
      report.setSuperPath(member.getSuperPath());
      report.setStatTime(DateUtil.dateToYMD(date));
      report.setAddTime(new Date());
      report.setWithdrawCount(0);
      report.setWithdrawMoney(BigDecimal.ZERO);
      report.setHandWithdrawCount(0);
      report.setHandWithdrawMoney(BigDecimal.ZERO);
      report.setFirstRechType(null);
      report.setFirstWithdrawMoney(BigDecimal.ZERO);
      report.setCounterFee(BigDecimal.ZERO);

      report.setBankCount(0);
      report.setBankMoney(BigDecimal.ZERO);
      report.setOnlineCount(0);
      report.setOnlineMoney(BigDecimal.ZERO);
      report.setHandRechCount(0);
      report.setHandRechMoney(BigDecimal.ZERO);
      report.setFirstRechType(null);
      report.setFirstRechMoney(BigDecimal.ZERO);
      report.setRechDiscount(BigDecimal.ZERO);
      report.setOtherDiscount(BigDecimal.ZERO);

      report.setExceptionWithdrawAmount(BigDecimal.ZERO);
      report.setExceptionRechargeAmount(BigDecimal.ZERO);

      report.setThirdWithdrawCount(0);
      report.setThirdWithdrawMoney(BigDecimal.ZERO);
      report.setVirtualRechargeMoney(BigDecimal.ZERO);
      report.setVirtualRechargeNumber(BigDecimal.ZERO);
      report.setVirtualWithdrawMoney(BigDecimal.ZERO);
      report.setVirtualWithdrawNumber(BigDecimal.ZERO);

      String[] superPaths = UserDlUtil.getDlAccount(member.getSuperPath());
      report.setDgdAccount(superPaths[0]);
      report.setGdAccount(superPaths[1]);
      report.setZdlAccount(superPaths[2]);
      report.setDlAccount(superPaths[3]);
    }
    return report;
  }

  public MemberRwReport queryByMemberAndDate(Long memberId, Date statTime) {
    return Optional.ofNullable(this.lambdaQuery().eq(MemberRwReport::getMemberId, memberId)
        .eq(MemberRwReport::getStatTime, DateUtil.dateToYMD(statTime)).one()).orElse(new MemberRwReport());
  }

}

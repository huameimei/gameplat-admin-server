package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.mapper.MemberRwReportMapper;
import com.gameplat.admin.service.MemberRwReportService;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.CashEnum;
import com.gameplat.common.enums.RechargeMode;
import com.gameplat.common.enums.TrueFalse;
import com.gameplat.common.util.Convert;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberRwReport;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.pay.PayType;
import com.gameplat.model.entity.recharge.RechargeOrder;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@Slf4j
public class MemberRwReportServiceImpl extends ServiceImpl<MemberRwReportMapper, MemberRwReport>
    implements MemberRwReportService {

  @Autowired
  private PayTypeService payTypeService;


  @Override
  public void addRecharge(Member member, Integer rechargeCount, RechargeOrder rechargeOrder) {

    log.info("充值订单数据：{}", JSONUtil.toJsonStr(rechargeOrder));
    MemberRwReport report = getOrCreateReportUserRw(member, rechargeOrder.getAuditTime());
    log.info("充值报表数据：{}", JSONUtil.toJsonStr(report));
    BigDecimal amount = rechargeOrder.getPayAmount();
    if (rechargeOrder.getPointFlag() == TrueFalse.TRUE.getValue()
        && BigDecimal.ZERO.compareTo(amount) < 0) {
      // 计算积分且充值金额大于 0 累加充值次数
      if (rechargeOrder.getMode() == RechargeMode.TRANSFER.getValue()) {
        report.setBankCount(report.getBankCount() + 1);
        report.setBankMoney(report.getBankMoney().add(amount));
      } else if (rechargeOrder.getMode() == RechargeMode.ONLINE_PAY.getValue()) {
        report.setOnlineCount(report.getOnlineCount() + 1);
        report.setOnlineMoney(report.getOnlineMoney().add(amount));
      } else if (rechargeOrder.getMode() == RechargeMode.MANUAL.getValue()) {
        report.setHandRechCount(report.getHandRechCount() + 1);
        report.setHandRechMoney(report.getHandRechMoney().add(amount));
      } else if (rechargeOrder.getMode() == RechargeMode.TRANSFER_VIP.getValue()) {
        report.setVipCount(report.getVipCount() + 1);
        report.setVipMoney(report.getVipMoney().add(amount));
      }
      // 计算首充
      if (rechargeCount == 0) {
        String firstRechMark = "首次充值";
        if (StringUtil.isNotBlank(rechargeOrder.getRemarks())
            && !rechargeOrder.getRemarks().contains(firstRechMark)) {
          rechargeOrder.setRemarks(rechargeOrder.getRemarks() + " [" + firstRechMark + "]");
        } else {
          rechargeOrder.setRemarks(firstRechMark);
        }
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
      if (ObjectUtil.isEmpty(report.getOtherDiscount())) {
        report.setOtherDiscount(BigDecimal.ZERO);
      }
      if (ObjectUtil.isEmpty(rechargeOrder.getDiscountAmount())) {
        rechargeOrder.setDiscountAmount(BigDecimal.ZERO);
      }
      report.setOtherDiscount(report.getOtherDiscount().add(rechargeOrder.getDiscountAmount()));
    }

    String payTypeCode = rechargeOrder.getPayType();
    if (ObjectUtil.isNotEmpty(payTypeCode)) {
      PayType payType = this.payTypeService.lambdaQuery().eq(PayType::getCode, payTypeCode).one();
      if (payType != null && payType.getBankFlag() == 2) {
        report.setVirtualRechargeNumber(
                report.getVirtualRechargeNumber().add(rechargeOrder.getCurrencyCount()));
        report.setVirtualRechargeMoney(
                report.getVirtualRechargeMoney().add(rechargeOrder.getAmount()));
      }
    }

    report.setMemberId(member.getId());
    this.saveOrUpdate(report);
  }

  @Override
  public void addWithdraw(Member member, Integer withdrawCount, MemberWithdraw memberWithdraw) {
    MemberRwReport report = getOrCreateReportUserRw(member, memberWithdraw.getOperatorTime());
    if (memberWithdraw.getPointFlag() == TrueFalse.TRUE.getValue()) {
      if (memberWithdraw.getCashMode() == CashEnum.CASH_MODE_HAND.getValue()) {
        report.setHandWithdrawCount(report.getHandWithdrawCount() + 1);
        report.setHandWithdrawMoney(
            report.getHandWithdrawMoney().add(memberWithdraw.getCashMoney()));
      } else if (memberWithdraw.getCashMode() == CashEnum.CASH_MODE_USER.getValue()) {
        report.setWithdrawCount(report.getWithdrawCount() + 1);
        report.setWithdrawMoney(report.getWithdrawMoney().add(memberWithdraw.getCashMoney()));
      } else if (memberWithdraw.getCashMode() == CashEnum.CASH_MODE_THIRD.getValue()) {
        report.setThirdWithdrawCount(report.getWithdrawCount() + 1);
        report.setThirdWithdrawMoney(
            report.getThirdWithdrawMoney().add(memberWithdraw.getCashMoney()));
      }
      // 计算首提
      if (withdrawCount == 0) {
        String firstWithdrawMark = "首次出款";
        if (StringUtil.isNotBlank(memberWithdraw.getCashReason())
            && !memberWithdraw.getCashReason().contains(firstWithdrawMark)) {
          memberWithdraw.setCashReason(
              memberWithdraw.getCashReason() + " [" + firstWithdrawMark + "]");
        } else {
          memberWithdraw.setCashReason(firstWithdrawMark);
        }
        report.setFirstWithdrawType(memberWithdraw.getCashMode());
        report.setFirstWithdrawMoney(
            report.getFirstWithdrawMoney().add(memberWithdraw.getCashMoney()));
      }
      if (!ObjectUtil.equals(memberWithdraw.getWithdrawType(), WithdrawTypeConstant.BANK)
              || !ObjectUtil.equals(memberWithdraw.getWithdrawType(), WithdrawTypeConstant.DIRECT)) {
        BigDecimal count =
            StringUtils.isNotEmpty(memberWithdraw.getCurrencyCount())
                ? Convert.toBigDecimal(memberWithdraw.getCurrencyCount())
                : BigDecimal.ZERO;
        report.setVirtualWithdrawNumber(report.getVirtualWithdrawNumber().add(count));
        report.setVirtualWithdrawMoney(
                report.getVirtualWithdrawMoney().add(memberWithdraw.getApproveMoney()));
      }

    } else {
      // 计算异常金额
      report.setExceptionWithdrawAmount(
          report.getExceptionWithdrawAmount().add(memberWithdraw.getCashMoney()));
    }
    this.saveOrUpdate(report);
  }

  private MemberRwReport getOrCreateReportUserRw(Member member, Date date) {
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
      report.setFirstWithdrawType(null);
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
      report.setVipCount(0);
      report.setVipMoney(BigDecimal.ZERO);
      report.setExceptionWithdrawAmount(BigDecimal.ZERO);
      report.setExceptionRechargeAmount(BigDecimal.ZERO);

      report.setThirdWithdrawCount(0);
      report.setThirdWithdrawMoney(BigDecimal.ZERO);
      report.setVirtualRechargeMoney(BigDecimal.ZERO);
      report.setVirtualRechargeNumber(BigDecimal.ZERO);
      report.setVirtualWithdrawMoney(BigDecimal.ZERO);
      report.setVirtualWithdrawNumber(BigDecimal.ZERO);

      String[] superPaths =
          this.getDlAccount(
              StringUtils.isEmpty(member.getSuperPath())
                  ? "/webRoot/" + report.getAccount()
                  : member.getSuperPath());
      // String[] superPaths = UserDlUtil.getDlAccount(member.getSuperPath());
      report.setDgdAccount(superPaths[0]);
      report.setGdAccount(superPaths[1]);
      report.setZdlAccount(superPaths[2]);
      report.setDlAccount(superPaths[3]);
    }
    return report;
  }

  public MemberRwReport queryByMemberAndDate(Long memberId, Date statTime) {
    return Optional.ofNullable(
            this.lambdaQuery()
                .eq(MemberRwReport::getMemberId, memberId)
                .eq(MemberRwReport::getStatTime, DateUtil.dateToYMD(statTime))
                .one())
        .orElse(new MemberRwReport());
  }

  /**
   * 用户代理路径
   *
   * @param userPaths String
   * @return String
   */
  private String[] getDlAccount(String userPaths) {
    String[] accounts = userPaths.substring(1).split("/");

    // 总代理账号
    String zdl = "";

    // 大股东账号
    String dgd = accounts[1];
    String gd = "";
    if (accounts.length > 2) {
      // 股东账号
      gd = accounts[2];
    }

    if (accounts.length > 3) {
      // 总代理账号
      zdl = accounts[3];
    }

    String dl = "";
    if (accounts.length > 4) {
      // 总代理账号
      dl = accounts[4];
    }

    return new String[] {dgd, gd, zdl, dl};
  }
}

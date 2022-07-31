package com.gameplat.admin.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.metadata.Pages;
import com.gameplat.model.entity.member.MemberWithdraw;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: francis
 * @create: 2022-08-01 00:09
 **/
@Component
public class WithdrawQueryCondition {

  public QueryWrapper<MemberWithdraw> buildQuerySql(MemberWithdrawQueryDTO dto) {
    QueryWrapper<MemberWithdraw> query = Wrappers.query();
    query
      .in(
        ObjectUtils.isNotNull(dto.getBankNameList()),
        "w.bank_name",
        dto.getBankNameList())
      .eq(
        ObjectUtils.isNotEmpty(dto.getSuperName()),
        "w.super_name",
        dto.getSuperName())
      .eq(
        ObjectUtils.isNotEmpty(dto.getBankCard()),
        "w.bank_card",
        dto.getBankCard())
      .eq(ObjectUtils.isNotEmpty(dto.getAccount()), "w.account", dto.getAccount())
      .ge(
        ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()),
        "w.cash_money",
        dto.getCashMoneyFrom())
      .le(
        ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()),
        "w.cash_money",
        dto.getCashMoneyFromTo())
      .eq(
        ObjectUtils.isNotEmpty(dto.getMemberType()),
        "w.member_type",
        dto.getMemberType())
      .eq(
        ObjectUtils.isNotEmpty(dto.getCashOrderNo()),
        "w.cash_order_no",
        dto.getCashOrderNo())
      .eq(
        ObjectUtils.isNotEmpty(dto.getOperatorAccount()),
        "w.operator_account",
        dto.getOperatorAccount())
      .ge(
        ObjectUtils.isNotNull(dto.getCreateTimeFrom()),
        "w.create_time",
        dto.getCreateTimeFrom())
      .le(
        ObjectUtils.isNotNull(dto.getCreateTimeTo()),
        "w.create_time",
        dto.getCreateTimeTo())
      .in(
        ObjectUtils.isNotNull(dto.getMemberLevelList()),
        "w.member_level",
        dto.getMemberLevelList());
    if (ObjectUtils.isNotNull(dto.getRechargeStatusList())
      && dto.getRechargeStatusList().size() > 0) {
      query
        .eq(
          dto.getRechargeStatusList().contains(3L),
          "w.withdraw_type",
          WithdrawTypeConstant.BANK)
        .eq(
          dto.getRechargeStatusList().contains(5L),
          "w.withdraw_type",
          WithdrawTypeConstant.DIRECT)
        .notIn(
          dto.getRechargeStatusList().contains(4L),
          "w.withdraw_type",
          WithdrawTypeConstant.BANK,
          WithdrawTypeConstant.MANUAL,
          WithdrawTypeConstant.DIRECT)
        .gt(dto.getRechargeStatusList().contains(6L), "w.counter_fee", 0);
    }
    if (ObjectUtils.isNotNull(dto.getCashStatusList())) {
      query.in("w.cash_status", dto.getCashStatusList());
    } else {
      query.le("w.cash_status", WithdrawStatus.HANDLED.getValue());
    }
    query.orderBy(
      ObjectUtils.isNotEmpty(dto.getOrder()),
      Pages.isAsc(dto.getOrder()),
      "w.create_time");
    return query;

  }
}

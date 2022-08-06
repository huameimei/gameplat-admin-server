package com.gameplat.admin.component;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.enums.OprateMode;
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.dto.MemberWithdrawQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawVO;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.WithdrawStatus;
import com.gameplat.common.metadata.Pages;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.member.MemberWithdrawHistory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description:
 * @author: francis
 * @create: 2022-08-01 00:09
 **/
@Component
public class WithdrawQueryCondition {

  /***提现创建时间 */
  private static final String ORDER_CREATE = "createTime";

  /***提现审核时间 */
  private static final String ORDER_OPERATOR = "operatorTime";

  /** 提现会员、代理 */
  private final String WITH_FORMAL_TYPE = "M";
  /** 提现推广 */
  private final String WITH_TEST_TYPE = "P";
  /** 查询会员类型 */
  private final String RECH_FORMAL_TYPE_QUERY = "M,A";

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

  public QueryWrapper<MemberWithdrawHistory> buildHistoryQuerySql(MemberWithdrawHistoryQueryDTO dto) {
    QueryWrapper<MemberWithdrawHistory> query = Wrappers.query();
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
        ObjectUtils.isNotEmpty(dto.getCashMode()),
        "w.cash_mode",
        dto.getCashMode())
      .eq(
        ObjectUtils.isNotEmpty(dto.getCashStatus()),
        "w.cash_status",
        dto.getCashStatus())
      .eq(
        ObjectUtils.isNotEmpty(dto.getPpMerchantId()),
        "w.pp_merchant_id",
        dto.getPpMerchantId())
      .eq(
        ObjectUtils.isNotEmpty(dto.getProxyPayStatus()),
        "w.proxy_pay_status",
        dto.getProxyPayStatus())
      .ge(
        ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()),
        "w.cash_money",
        dto.getCashMoneyFrom())
      .le(
        ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()),
        "w.cash_money",
        dto.getCashMoneyFromTo())
      .in(
        ObjectUtils.isNotEmpty(dto.getMemberType())
          && dto.getMemberType().equalsIgnoreCase(WITH_FORMAL_TYPE),
        "w.cash_money",
        RECH_FORMAL_TYPE_QUERY.split(","))
      .eq(
        ObjectUtils.isNotEmpty(dto.getMemberType())
          && dto.getMemberType().equalsIgnoreCase(WITH_TEST_TYPE),
        "w.member_type",
        dto.getMemberType())
      .eq(
        ObjectUtils.isNotEmpty(dto.getCashOrderNo()),
        "w.cash_order_no",
        dto.getCashOrderNo())
      .in(
        ObjectUtils.isNotNull(dto.getMemberLevelList()),
        "w.member_level",
        dto.getMemberLevelList())
      .eq(
        ObjectUtils.isNotEmpty(dto.getBankCard()),
        "w.bank_card",
        dto.getBankCard());
    if (ObjectUtils.isNotEmpty(dto.getOrderBy())
      && ObjectUtil.equal(ORDER_OPERATOR, dto.getOrderBy())) {
      query
        .ge(
          ObjectUtils.isNotNull(dto.getStartDate()),
          "w.operator_time",
          dto.getStartDate())
        .le(
          ObjectUtils.isNotNull(dto.getEndDate()),
          "w.operator_time",
          dto.getEndDate());
    }
    if (ObjectUtils.isNotEmpty(dto.getOrderBy())
      && ObjectUtil.equal(ORDER_CREATE, dto.getOrderBy())) {
      query
        .ge(
          ObjectUtils.isNotNull(dto.getStartDate()),
          "w.create_time",
          dto.getStartDate())
        .le(
          ObjectUtils.isNotNull(dto.getEndDate()),
          "w.create_time",
          dto.getEndDate());
    }
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(
        "w.account",
        Arrays.asList(StringUtils.split(dto.getAccounts(), ",")));
    }
    if (ObjectUtils.isNotEmpty(dto.getOperatorAccounts())) {
      query.in(
        "w.operator_account",
        Arrays.asList(StringUtils.split(dto.getOperatorAccounts())));
    }
    if (dto.isAllSubs()) {
      query.like(
        ObjectUtils.isNotEmpty(dto.getSuperName()),
        "w.super_path",
        dto.getSuperName());
    } else {
      query.eq(
        ObjectUtils.isNotEmpty(dto.getSuperName()),
        "w.super_name",
        dto.getSuperName());
    }
    if (OprateMode.OPRATE_MANUAL.match(dto.getOprateMode())) {
      query
        .isNull("w.pp_merchant_id")
        .eq("w.withdraw_type", WithdrawTypeConstant.BANK);
    }
    if (OprateMode.OPRATE_ATUO.match(dto.getOprateMode())) {
      query.isNotNull("w.pp_merchant_id");
    }
    if (OprateMode.OPRATE_VIRTUAL.match(dto.getOprateMode())) {
      query.notIn("w.withdraw_type", WithdrawTypeConstant.BANK, WithdrawTypeConstant.MANUAL, WithdrawTypeConstant.DIRECT);
    }
    if (OprateMode.OPRATE_MODE.match(dto.getOprateMode())) {
      query.eq("w.withdraw_type", WithdrawTypeConstant.DIRECT);
    }
    query.orderBy(
      ObjectUtils.isNotEmpty(dto.getOrder()),
      !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
      "createTime".equals(dto.getOrderBy())
        ? "w.create_time"
        : "w.operator_time");
    return query;
  }
}

package com.gameplat.admin.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.model.entity.recharge.RechargeOrder;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: francis
 * @create: 2022-07-31 19:07
 **/
@Component
public class RechargeOrderQueryCondition {

  public QueryWrapper<RechargeOrder> builderQueryWrapper(RechargeOrderQueryDTO dto) {

    QueryWrapper<RechargeOrder> query = Wrappers.query();
    query
      .in(ObjectUtils.isNotNull(dto.getModeList()), "r.mode", dto.getModeList())
      .in(
        ObjectUtils.isNotNull(dto.getStatusList()),
        "r.status",
        dto.getStatusList())
      .eq(
        ObjectUtils.isNotEmpty(dto.getPointFlag()),
        "r.point_flag",
        dto.getPointFlag())
      .in(
        ObjectUtils.isNotNull(dto.getPayAccountList()),
        "r.pay_account_account",
        dto.getPayAccountList())
      .eq(
        ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
        "r.tp_merchant_id",
        dto.getTpMerchantId())
      .ge(
        ObjectUtils.isNotEmpty(dto.getAmountFrom()),
        "r.amount",
        dto.getAmountFrom())
      .le(ObjectUtils.isNotEmpty(dto.getAmountTo()), "r.amount", dto.getAmountTo())
      .eq(ObjectUtils.isNotEmpty(dto.getAccount()), "r.account", dto.getAccount())
      .in(
        ObjectUtils.isNotEmpty(dto.getMemberType())
          && MemberEnums.Type.MEMBER.match(dto.getMemberType()),
        "r.member_type",
        MemberEnums.Type.MEMBER.value(),
        MemberEnums.Type.AGENT.value())
      .eq(
        ObjectUtils.isNotEmpty(dto.getMemberType())
          && MemberEnums.Type.PROMOTION.match(dto.getMemberType()),
        "r.member_type",
        dto.getMemberType())
      .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), "r.order_no", dto.getOrderNo())
      .eq(
        ObjectUtils.isNotEmpty(dto.getSuperAccount()),
        "r.super_account",
        dto.getSuperAccount())
      .ge(
        ObjectUtils.isNotEmpty(dto.getCreateTimeFrom()),
        "r.create_time",
        dto.getCreateTimeFrom())
      .le(
        ObjectUtils.isNotEmpty(dto.getCreateTimeTo()),
        "r.create_time",
        dto.getCreateTimeTo())
      .in(
        ObjectUtils.isNotNull(dto.getMemberLevelList()),
        "r.member_level",
        dto.getMemberLevelList());
    if (dto.isFullNameFuzzy()) {
      query.like(
        ObjectUtils.isNotEmpty(dto.getFullName()), "r.nickname", dto.getFullName());
    } else {
      query.eq(
        ObjectUtils.isNotEmpty(dto.getFullName()), "r.nickname", dto.getFullName());
    }
    query.orderBy(
      ObjectUtils.isNotEmpty(dto.getOrder()),
      !ObjectUtils.isEmpty(dto.getOrder()) && dto.getOrder().equals("ASC"),
      "r.create_time");
    return query;

  }

}

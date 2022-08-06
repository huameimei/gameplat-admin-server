package com.gameplat.admin.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.model.entity.recharge.RechargeOrder;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description:
 * @author: francis
 * @create: 2022-07-31 19:07
 */
@Component
public class RechargeOrderQueryCondition {

  private final String RECH_TEST_TYPE = "P";
  private final String RECH_FORMAL_TYPE_QUERY = "M,A";
  private final String RECH_FORMAL_TYPE = "M";

  public QueryWrapper<RechargeOrderHistory> builderHistoryQueryWrapper(
          RechargeOrderHistoryQueryDTO dto) {

    QueryWrapper<RechargeOrderHistory> query = Wrappers.query();
    query
            .in(ObjectUtils.isNotNull(dto.getModeList()), "r.mode", dto.getModeList())
            .eq(ObjectUtils.isNotEmpty(dto.getPointFlag()), "r.point_flag", dto.getPointFlag())
            .in(
                    ObjectUtils.isNotEmpty(dto.getDiscountTypes()),
                    "r.discount_type",
                    dto.getDiscountTypes())
            .in(ObjectUtils.isNotNull(dto.getPayTypeList()), "r.pay_type", dto.getPayTypeList())
            .eq(ObjectUtils.isNotEmpty(dto.getStatus()), "r.status", dto.getStatus())
            .in(
                    ObjectUtils.isNotEmpty(dto.getPayAccountOwnerList()),
                    "r.pay_account_account",
                    dto.getPayAccountOwnerList())
            .eq(
                    ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
                    "r.tp_merchant_id",
                    dto.getTpMerchantId())
            .ge(ObjectUtils.isNotEmpty(dto.getAmountFrom()), "r.amount", dto.getAmountFrom())
            .le(ObjectUtils.isNotEmpty(dto.getAmountTo()), "r.amount", dto.getAmountTo())
            .in(
                    ObjectUtils.isNotEmpty(dto.getMemberType())
                            && dto.getMemberType().equalsIgnoreCase(RECH_FORMAL_TYPE),
                    "r.member_type",
                    RECH_FORMAL_TYPE_QUERY.split(","))
            .eq(
                    ObjectUtils.isNotEmpty(dto.getMemberType())
                            && dto.getMemberType().equalsIgnoreCase(RECH_TEST_TYPE),
                    "r.member_type",
                    dto.getMemberType())
            .in(
                    ObjectUtils.isNotNull(dto.getMemberLevelList()),
                    "r.member_level",
                    dto.getMemberLevelList())
            .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), "r.order_no", dto.getOrderNo())
            .eq(ObjectUtils.isNotEmpty(dto.getSuperAccount()), "r.super_account", dto.getSuperAccount())
            .like(ObjectUtils.isNotEmpty(dto.getRemarks()), "r.remarks", dto.getRemarks())
            .like(
                    ObjectUtils.isNotEmpty(dto.getAuditRemarks()), "r.audit_remarks", dto.getAuditRemarks())
            .eq(ObjectUtils.isNotEmpty(dto.getOrderNo()), "r.order_no", dto.getOrderNo());
    if (ObjectUtils.isNotEmpty(dto.getAuditorAccounts())) {
      query.in("r.auditor_account", dto.getAuditorAccounts().split(","));
    }
    if (ObjectUtils.isEmpty(dto.getOrderBy()) || dto.getOrderBy().equals("createTime")) {
      query.between(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              "r.create_time",
              dto.getBeginDatetime(),
              dto.getEndDatetime());
    } else {
      query.between(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              "r.audit_time",
              dto.getBeginDatetime(),
              dto.getEndDatetime());
    }
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in("r.account", dto.getAccounts().split(","));
    }
    if (dto.isSuperPathLike()) {
      query.like(
              ObjectUtils.isNotEmpty(dto.getSuperAccount()), "r.super_account", dto.getSuperAccount());
    } else {
      query.eq(
              ObjectUtils.isNotEmpty(dto.getSuperAccount()), "r.super_account", dto.getSuperAccount());
    }
    query.orderBy(
            ObjectUtils.isNotEmpty(dto.getOrder()),
            !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
            "createTime".equals(dto.getOrderBy()) ? "r.create_time" : "r.audit_time");
    return query;
  }

  public QueryWrapper<RechargeOrder> builderQueryWrapper(RechargeOrderQueryDTO dto) {

    QueryWrapper<RechargeOrder> query = Wrappers.query();
    query
            .in(ObjectUtils.isNotNull(dto.getModeList()), "r.mode", dto.getModeList())
            .in(ObjectUtils.isNotNull(dto.getStatusList()), "r.status", dto.getStatusList())
            .eq(ObjectUtils.isNotEmpty(dto.getPointFlag()), "r.point_flag", dto.getPointFlag())
            .in(
                    ObjectUtils.isNotNull(dto.getPayAccountList()),
                    "r.pay_account_account",
                    dto.getPayAccountList())
            .eq(
                    ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
                    "r.tp_merchant_id",
                    dto.getTpMerchantId())
            .ge(ObjectUtils.isNotEmpty(dto.getAmountFrom()), "r.amount", dto.getAmountFrom())
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
            .eq(ObjectUtils.isNotEmpty(dto.getSuperAccount()), "r.super_account", dto.getSuperAccount())
            .ge(
                    ObjectUtils.isNotEmpty(dto.getCreateTimeFrom()),
                    "r.create_time",
                    dto.getCreateTimeFrom())
            .le(ObjectUtils.isNotEmpty(dto.getCreateTimeTo()), "r.create_time", dto.getCreateTimeTo())
            .in(
                    ObjectUtils.isNotNull(dto.getMemberLevelList()),
                    "r.member_level",
                    dto.getMemberLevelList());
    if (dto.isFullNameFuzzy()) {
      query.like(ObjectUtils.isNotEmpty(dto.getFullName()), "r.nickname", dto.getFullName());
    } else {
      query.eq(ObjectUtils.isNotEmpty(dto.getFullName()), "r.nickname", dto.getFullName());
    }
    query.orderBy(
            ObjectUtils.isNotEmpty(dto.getOrder()),
            !ObjectUtils.isEmpty(dto.getOrder()) && dto.getOrder().equals("ASC"),
            "r.create_time");
    return query;
  }
}

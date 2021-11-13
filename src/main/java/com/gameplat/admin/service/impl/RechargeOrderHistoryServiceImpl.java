package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.RechargeOrderHistoryConvert;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.domain.RechargeOrderHistory;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RechargeOrderHistoryServiceImpl
    extends ServiceImpl<RechargeOrderHistoryMapper, RechargeOrderHistory>
    implements RechargeOrderHistoryService {

  @Autowired private RechargeOrderHistoryConvert rechargeOrderHistoryConvert;

  @Override
  public IPage<RechargeOrderHistoryVO> findPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrderHistory> query = Wrappers.lambdaQuery();
    query
        .in(
            ObjectUtils.isNotNull(dto.getModeList()),
            RechargeOrderHistory::getMode,
            dto.getModeList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPointFlag()),
            RechargeOrderHistory::getPointFlag,
            dto.getPointFlag())
        .in(
            ObjectUtils.isNotEmpty(dto.getDiscountTypes()),
            RechargeOrderHistory::getDiscountType,
            dto.getDiscountTypes())
        .in(
            ObjectUtils.isNotNull(dto.getPayTypeList()),
            RechargeOrderHistory::getPayType,
            dto.getPayTypeList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getStatus()),
            RechargeOrderHistory::getStatus,
            dto.getStatus())
        .in(
            ObjectUtils.isNotEmpty(dto.getPayAccountOwnerList()),
            RechargeOrderHistory::getPayAccountOwner,
            dto.getPayAccountOwnerList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getTpMerchantId()),
            RechargeOrderHistory::getTpMerchantId,
            dto.getTpMerchantId())
        .ge(
            ObjectUtils.isNotEmpty(dto.getAmountFrom()),
            RechargeOrderHistory::getAmount,
            dto.getAmountFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getAmountTo()),
            RechargeOrderHistory::getAmount,
            dto.getAmountTo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType()),
            RechargeOrderHistory::getMemberType,
            dto.getMemberType())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            RechargeOrderHistory::getMemberLevel,
            dto.getMemberLevelList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOrderNo()),
            RechargeOrderHistory::getOrderNo,
            dto.getOrderNo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperAccount()),
            RechargeOrderHistory::getSuperAccount,
            dto.getSuperAccount())
        .like(
            ObjectUtils.isNotEmpty(dto.getRemarks()),
            RechargeOrderHistory::getRemarks,
            dto.getRemarks())
        .like(
            ObjectUtils.isNotEmpty(dto.getAuditRemarks()),
            RechargeOrderHistory::getAuditRemarks,
            dto.getAuditRemarks())
        .eq(
            ObjectUtils.isNotEmpty(dto.getOrderNo()),
            RechargeOrderHistory::getOrderNo,
            dto.getOrderNo())
        .in(
            ObjectUtils.isNotEmpty(dto.getAuditorAccounts()),
            RechargeOrderHistory::getAuditorAccount,
            Arrays.asList(StringUtils.split(dto.getAuditorAccounts(), ",")));
    if (ObjectUtils.isEmpty(dto.getOrderBy()) || "createTime".equals(dto.getOrderBy())) {
      query
          .ge(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              RechargeOrderHistory::getCreateTime,
              dto.getBeginDatetime())
          .le(
              ObjectUtils.isNotEmpty(dto.getEndDatetime()),
              RechargeOrderHistory::getCreateTime,
              dto.getEndDatetime());
    } else {
      query
          .ge(
              ObjectUtils.isNotEmpty(dto.getBeginDatetime()),
              RechargeOrderHistory::getAuditTime,
              dto.getBeginDatetime())
          .le(
              ObjectUtils.isNotEmpty(dto.getEndDatetime()),
              RechargeOrderHistory::getAuditTime,
              dto.getEndDatetime());
    }
    query.in(
        ObjectUtils.isNotEmpty(dto.getAccounts()),
        RechargeOrderHistory::getAccount,
        Arrays.asList(StringUtils.split(dto.getAccounts(), ",")));
    if (dto.isSuperPathLike()) {
      query.like(
          ObjectUtils.isNotEmpty(dto.getSuperAccount()),
          RechargeOrderHistory::getSuperPath,
          dto.getSuperAccount());
    } else {
      query.eq(
          ObjectUtils.isNotEmpty(dto.getSuperAccount()),
          RechargeOrderHistory::getSuperAccount,
          dto.getSuperAccount());
    }
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
        "createTime".equals(dto.getOrderBy())
            ? RechargeOrderHistory::getCreateTime
            : RechargeOrderHistory::getAuditTime);

    return this.page(page, query).convert(rechargeOrderHistoryConvert::toVo);
  }
}

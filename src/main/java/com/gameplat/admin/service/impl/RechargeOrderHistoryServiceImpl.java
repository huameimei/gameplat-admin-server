package com.gameplat.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.RechargeOrderHistoryConvert;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class RechargeOrderHistoryServiceImpl
    extends ServiceImpl<RechargeOrderHistoryMapper, RechargeOrderHistory>
    implements RechargeOrderHistoryService {

  @Autowired private RechargeOrderHistoryConvert rechargeOrderHistoryConvert;

  @Autowired private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

  @Override
  public IPage<RechargeOrderHistoryVO> findPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto) {
    LambdaQueryWrapper<RechargeOrderHistory> query = buildSql(dto);
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        ObjectUtils.isEmpty(dto.getOrder()) ? false : dto.getOrder().equals("ASC"),
        dto.getOrderBy().equals("createTime")
            ? RechargeOrderHistory::getCreateTime
            : RechargeOrderHistory::getAuditTime);
    return this.page(page, query).convert(rechargeOrderHistoryConvert::toVo);
  }

  @Override
  public RechargeHistorySummaryVO findSumRechargeOrderHistory(RechargeOrderHistoryQueryDTO dto) {
    dto.setAuditTime(DateUtil.endOfDay(DateUtil.yesterday()));
    LambdaQueryWrapper<RechargeOrderHistory> query = buildSql(dto);
    return rechargeOrderHistoryMapper.summaryRechargeOrderHistory(query);
  }

  private LambdaQueryWrapper<RechargeOrderHistory> buildSql(RechargeOrderHistoryQueryDTO dto) {
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
        .le(
            ObjectUtils.isNotNull(dto.getAuditTime()),
            RechargeOrderHistory::getAuditTime,
            dto.getAuditTime());
    if (ObjectUtils.isNotEmpty(dto.getAuditorAccounts())) {
      query.in(RechargeOrderHistory::getAuditorAccount, dto.getAuditorAccounts().split(","));
    }
    if (ObjectUtils.isEmpty(dto.getOrderBy()) || dto.getOrderBy().equals("createTime")) {
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
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(RechargeOrderHistory::getAccount, dto.getAccounts().split(","));
    }
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
    return query;
  }
}

package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWithdrawHistoryConvert;
import com.gameplat.admin.enums.OprateMode;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.dto.UserWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import com.gameplat.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWithdrawHistoryServiceImpl
    extends ServiceImpl<MemberWithdrawHistoryMapper, MemberWithdrawHistory>
    implements MemberWithdrawHistoryService {

  @Autowired private MemberWithdrawHistoryConvert userWithdrawHistoryConvert;

  @Override
  public IPage<MemberWithdrawHistoryVO> findPage(
      Page<MemberWithdrawHistory> page, UserWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = Wrappers.lambdaQuery();
    query
        .in(
            ObjectUtils.isNotNull(dto.getBankNameList()),
            MemberWithdrawHistory::getBankName,
            dto.getBankNameList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getSuperName()),
            MemberWithdrawHistory::getSuperName,
            dto.getSuperName())
        .eq(
            ObjectUtils.isNotEmpty(dto.getCashMode()),
            MemberWithdrawHistory::getCashMode,
            dto.getCashMode())
        .eq(
            ObjectUtils.isNotEmpty(dto.getCashStatus()),
            MemberWithdrawHistory::getCashStatus,
            dto.getCashStatus())
        .eq(
            ObjectUtils.isNotEmpty(dto.getPpMerchantId()),
            MemberWithdrawHistory::getPpMerchantId,
            dto.getPpMerchantId())
        .eq(
            ObjectUtils.isNotEmpty(dto.getProxyPayStatus()),
            MemberWithdrawHistory::getProxyPayStatus,
            dto.getProxyPayStatus())
        .ge(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()),
            MemberWithdrawHistory::getCashMoney,
            dto.getCashMoneyFrom())
        .le(
            ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()),
            MemberWithdrawHistory::getCashMoney,
            dto.getCashMoneyFromTo())
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType()),
            MemberWithdrawHistory::getMemberType,
            dto.getMemberType())
        .eq(
            ObjectUtils.isNotEmpty(dto.getCashOrderNo()),
            MemberWithdrawHistory::getCashOrderNo,
            dto.getCashOrderNo())
        .in(
            ObjectUtils.isNotNull(dto.getMemberLevelList()),
            MemberWithdrawHistory::getMemberLevel,
            dto.getMemberLevelList())
        .eq(
            ObjectUtils.isNotEmpty(dto.getBankCard()),
            MemberWithdrawHistory::getBankCard,
            dto.getBankCard());
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(
          MemberWithdrawHistory::getAccount,
          Arrays.asList(StringUtils.split(dto.getAccounts(), ",")));
    }
    if (ObjectUtils.isNotEmpty(dto.getOperatorAccounts())) {
      query.in(
          MemberWithdrawHistory::getOperatorAccount,
          Arrays.asList(StringUtils.split(dto.getOperatorAccounts(), ",")));
    }
    if (dto.isAllSubs()) {
      query.like(
          ObjectUtils.isNotEmpty(dto.getSuperName()),
          MemberWithdrawHistory::getSuperPath,
          dto.getSuperName());
    } else {
      query.eq(
          ObjectUtils.isNotEmpty(dto.getSuperName()),
          MemberWithdrawHistory::getSuperName,
          dto.getSuperName());
    }
    if (OprateMode.OPRATE_MANUAL.match(dto.getOprateMode())) {
      query
          .isNull(MemberWithdrawHistory::getPpMerchantId)
          .eq(MemberWithdrawHistory::getWithdrawType, "BANK");
    }
    if (OprateMode.OPRATE_ATUO.match(dto.getOprateMode())) {
      query.isNotNull(MemberWithdrawHistory::getPpMerchantId);
    }
    if (OprateMode.OPRATE_VIRTUAL.match(dto.getOprateMode())) {
      query.notIn(MemberWithdrawHistory::getWithdrawType, "BANK", "MANUAL", "DIRECT");
    }
    if (OprateMode.OPRATE_MODE.match(dto.getOprateMode())) {
      query.eq(MemberWithdrawHistory::getWithdrawType, "DIRECT");
    }
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
        "createTime".equals(dto.getOrderBy())
            ? MemberWithdrawHistory::getCreateTime
            : MemberWithdrawHistory::getOperatorTime);
    return this.page(page, query).convert(userWithdrawHistoryConvert::toVo);
  }
}

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
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistorySummaryVO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWithdrawHistoryServiceImpl extends
    ServiceImpl<MemberWithdrawHistoryMapper, MemberWithdrawHistory>
    implements MemberWithdrawHistoryService {

  @Autowired
  private MemberWithdrawHistoryConvert userWithdrawHistoryConvert;

  @Autowired
  private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

  @Override
  public IPage<MemberWithdrawHistoryVO> findPage(Page<MemberWithdrawHistory> page,
      MemberWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = buildSql(dto);
    query.orderBy(ObjectUtils.isNotEmpty(dto.getOrder()),
        ObjectUtils.isEmpty(dto.getOrder()) ? false : dto.getOrder().equals("ASC"),
        dto.getOrderBy().equals("createTime") ? MemberWithdrawHistory::getCreateTime
            : MemberWithdrawHistory::getOperatorTime);
    return this.page(page, query).convert(userWithdrawHistoryConvert::toVo);
  }

  @Override
  public MemberWithdrawHistorySummaryVO findSumMemberWithdrawHistory(
      MemberWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = buildSql(dto);
    return memberWithdrawHistoryMapper.summaryMemberWithdrawHistory(query);
  }

  private LambdaQueryWrapper buildSql(MemberWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = Wrappers.lambdaQuery();
    query.in(ObjectUtils.isNotNull(dto.getBankNameList()), MemberWithdrawHistory::getBankName,
        dto.getBankNameList())
        .eq(ObjectUtils.isNotEmpty(dto.getSuperName()), MemberWithdrawHistory::getSuperName,
            dto.getSuperName())
        .eq(ObjectUtils.isNotEmpty(dto.getCashMode()), MemberWithdrawHistory::getCashMode,
            dto.getCashMode())
        .eq(ObjectUtils.isNotEmpty(dto.getCashStatus()), MemberWithdrawHistory::getCashStatus,
            dto.getCashStatus())
        .eq(ObjectUtils.isNotEmpty(dto.getPpMerchantId()), MemberWithdrawHistory::getPpMerchantId,
            dto.getPpMerchantId())
        .eq(ObjectUtils.isNotEmpty(dto.getProxyPayStatus()),
            MemberWithdrawHistory::getProxyPayStatus,
            dto.getProxyPayStatus())
        .ge(ObjectUtils.isNotEmpty(dto.getCashMoneyFrom()), MemberWithdrawHistory::getCashMoney,
            dto.getCashMoneyFrom())
        .le(ObjectUtils.isNotEmpty(dto.getCashMoneyFromTo()), MemberWithdrawHistory::getCashMoney,
            dto.getCashMoneyFromTo())
        .eq(ObjectUtils.isNotEmpty(dto.getMemberType()), MemberWithdrawHistory::getMemberType,
            dto.getMemberType())
        .eq(ObjectUtils.isNotEmpty(dto.getCashOrderNo()), MemberWithdrawHistory::getCashOrderNo,
            dto.getCashOrderNo())
        .in(ObjectUtils.isNotNull(dto.getMemberLevelList()), MemberWithdrawHistory::getMemberLevel,
            dto.getMemberLevelList())
        .eq(ObjectUtils.isNotEmpty(dto.getBankCard()), MemberWithdrawHistory::getBankCard,
            dto.getBankCard());
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(MemberWithdrawHistory::getAccount, dto.getAccounts().split(","));
    }
    if (ObjectUtils.isNotEmpty(dto.getOperatorAccounts())) {
      query.in(MemberWithdrawHistory::getOperatorAccount, dto.getOperatorAccounts().split(","));
    }
    if (dto.isAllSubs()) {
      query.like(ObjectUtils.isNotEmpty(dto.getSuperName()),
          MemberWithdrawHistory::getSuperPath,
          dto.getSuperName());
    } else {
      query.eq(ObjectUtils.isNotEmpty(dto.getSuperName()),
          MemberWithdrawHistory::getSuperName,
          dto.getSuperName());
    }
    if (OprateMode.OPRATE_MANUAL.value() == dto.getOprateMode()) {
      query.isNull(MemberWithdrawHistory::getPpMerchantId)
          .eq(MemberWithdrawHistory::getWithdrawType, "BANK");
    }
    if (OprateMode.OPRATE_ATUO.value() == dto.getOprateMode()) {
      query.isNotNull(MemberWithdrawHistory::getPpMerchantId);
    }
    if (OprateMode.OPRATE_VIRTUAL.value() == dto.getOprateMode()) {
      query.notIn(MemberWithdrawHistory::getWithdrawType,
          "BANK", "MANUAL", "DIRECT");
    }
    if (OprateMode.OPRATE_MODE.value() == dto.getOprateMode()) {
      query.eq(MemberWithdrawHistory::getWithdrawType, "DIRECT");
    }
    return query;
  }


}

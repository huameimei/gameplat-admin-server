package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.WithdrawTypeConstant;
import com.gameplat.admin.convert.MemberWithdrawHistoryConvert;
import com.gameplat.admin.enums.OprateMode;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.model.dto.MemberWithdrawHistoryQueryDTO;
import com.gameplat.admin.model.vo.MemberWithdrawHistorySummaryVO;
import com.gameplat.admin.model.vo.MemberWithdrawHistoryVO;
import com.gameplat.admin.service.MemberWithdrawHistoryService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.member.MemberWithdraw;
import com.gameplat.model.entity.member.MemberWithdrawHistory;
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

  /** 提现会员、代理 */
  private final String WITH_FORMAL_TYPE = "HY";
  /** 提现推广 */
  private final String WITH_TEST_TYPE = "VHY";
  /** 查询会员类型 */
  private final String RECH_FORMAL_TYPE_QUERY = "M,A";

  @Autowired private MemberWithdrawHistoryConvert userWithdrawHistoryConvert;

  @Autowired(required = false)
  private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

  @Override
  public IPage<MemberWithdrawHistoryVO> findPage(
      Page<MemberWithdrawHistory> page, MemberWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = buildSql(dto);
    query.orderBy(
        ObjectUtils.isNotEmpty(dto.getOrder()),
        !ObjectUtils.isEmpty(dto.getOrder()) && "ASC".equals(dto.getOrder()),
        "createTime".equals(dto.getOrderBy())
            ? MemberWithdrawHistory::getCreateTime
            : MemberWithdrawHistory::getOperatorTime);

    return this.page(page, query).convert(userWithdrawHistoryConvert::toVo);
  }

  @Override
  public MemberWithdrawHistorySummaryVO findSumMemberWithdrawHistory(
      MemberWithdrawHistoryQueryDTO dto) {
    LambdaQueryWrapper<MemberWithdrawHistory> query = buildSql(dto);
    return memberWithdrawHistoryMapper.summaryMemberWithdrawHistory(query);
  }

  private LambdaQueryWrapper<MemberWithdrawHistory> buildSql(MemberWithdrawHistoryQueryDTO dto) {
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
        /* .eq(
        ObjectUtils.isNotEmpty(dto.getMemberType()),
        MemberWithdrawHistory::getMemberType,
        dto.getMemberType())*/
        .in(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && dto.getMemberType().equalsIgnoreCase(WITH_FORMAL_TYPE),
            MemberWithdrawHistory::getMemberType,
            RECH_FORMAL_TYPE_QUERY.split(","))
        .eq(
            ObjectUtils.isNotEmpty(dto.getMemberType())
                && dto.getMemberType().equalsIgnoreCase(WITH_TEST_TYPE),
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
            dto.getBankCard())
        .ge(
            ObjectUtils.isNotNull(dto.getStartDate()),
            MemberWithdrawHistory::getCreateTime,
            dto.getStartDate())
        .le(
            ObjectUtils.isNotNull(dto.getEndDate()),
            MemberWithdrawHistory::getCreateTime,
            dto.getEndDate());
    if (ObjectUtils.isNotEmpty(dto.getAccounts())) {
      query.in(
          MemberWithdrawHistory::getAccount,
          Arrays.asList(StringUtils.split(dto.getAccounts(), ",")));
    }
    if (ObjectUtils.isNotEmpty(dto.getOperatorAccounts())) {
      query.in(
          MemberWithdrawHistory::getOperatorAccount,
          Arrays.asList(StringUtils.split(dto.getOperatorAccounts())));
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
          .eq(MemberWithdrawHistory::getWithdrawType, WithdrawTypeConstant.BANK);
    }
    if (OprateMode.OPRATE_ATUO.match(dto.getOprateMode())) {
      query.isNotNull(MemberWithdrawHistory::getPpMerchantId);
    }
    if (OprateMode.OPRATE_VIRTUAL.match(dto.getOprateMode())) {
      query.notIn(MemberWithdrawHistory::getWithdrawType, WithdrawTypeConstant.BANK, WithdrawTypeConstant.MANUAL, WithdrawTypeConstant.DIRECT);
    }
    if (OprateMode.OPRATE_MODE.match(dto.getOprateMode())) {
      query.eq(MemberWithdrawHistory::getWithdrawType, WithdrawTypeConstant.DIRECT);
    }
    return query;
  }
}

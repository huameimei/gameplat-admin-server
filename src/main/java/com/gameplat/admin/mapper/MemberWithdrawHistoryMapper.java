package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberWithdrawHistory;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.model.vo.MemberWithdrawHistorySummaryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MemberWithdrawHistoryMapper extends BaseMapper<MemberWithdrawHistory> {

  @Select("SELECT COUNT(DISTINCT member_id) as memberCount, COUNT(1) AS cashCount, sum(cash_money) AS cashMoney, sum(approve_money) AS approveMoney,"
      + "             sum(counter_fee) AS counterFee, "
      + "             sum(approve_currency_count) as approveCurrencyCount "
      + "             FROM member_withdraw_history "
      + "             ${ew.customSqlSegment}")
  MemberWithdrawHistorySummaryVO summaryMemberWithdrawHistory(
      @Param(Constants.WRAPPER) Wrapper<MemberWithdrawHistory> wrapper);

  /** 提现IP统计 */
  List<IpStatisticsVO> findIp(QueryIpStatReportDTO dto);

  /** 提现IP分析 */
  IPage<IpAnalysisVO> page (PageDTO<IpAnalysisVO> page, @Param("dto") IpAnalysisDTO dto);
}

package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RechargeOrderHistoryMapper extends BaseMapper<RechargeOrderHistory> {

  @Select(
      "SELECT COUNT(DISTINCT member_id) as memberCount, COUNT(1) AS rechargeCount, sum(pay_amount) AS amount, sum(discount_amount) AS discountAmount,"
          + "             sum(total_amount) AS totalAmount, "
          + "             sum(currency_count) as currencyCount "
          + "             FROM recharge_order_history "
          + "             ${ew.customSqlSegment}")
  RechargeHistorySummaryVO summaryRechargeOrderHistory(
      @Param(Constants.WRAPPER) Wrapper<RechargeOrderHistory> wrapper);

  /** 充值IP统计 */
  List<IpStatisticsVO> findIp(QueryIpStatReportDTO dto);

  /** 充值IP分析 */
  IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, @Param("dto") IpAnalysisDTO dto);

  IPage<RechargeOrderHistoryVO> findPage(Page<RechargeOrderHistory> page, @Param(Constants.WRAPPER) QueryWrapper<RechargeOrderHistory> queryWrapper);
}

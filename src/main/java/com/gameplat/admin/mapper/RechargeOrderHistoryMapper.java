package com.gameplat.admin.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gameplat.admin.model.domain.RechargeOrderHistory;
import com.gameplat.admin.model.dto.QueryIpStatReportDTO;
import com.gameplat.admin.model.vo.IpStatisticsVO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RechargeOrderHistoryMapper extends BaseMapper<RechargeOrderHistory> {

  @Select("SELECT COUNT(DISTINCT member_id) as memberCount, COUNT(1) AS rechargeCount, sum(amount) AS amount, sum(discount_amount) AS discountAmount,"
      + "             sum(total_amount) AS totalAmount, "
      + "             sum(currency_count) as currencyCount "
      + "             FROM recharge_order_history "
      + "             ${ew.customSqlSegment}")
  RechargeHistorySummaryVO summaryRechargeOrderHistory(
      @Param(Constants.WRAPPER) Wrapper<RechargeOrderHistory> wrapper);

  /** 充值IP统计 */
  List<IpStatisticsVO> findIp(QueryIpStatReportDTO dto);
}

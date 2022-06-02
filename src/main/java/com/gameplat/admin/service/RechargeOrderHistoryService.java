package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RechargeOrderHistoryService extends IService<RechargeOrderHistory> {

  IPage<RechargeOrderHistoryVO> findPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto);

  RechargeHistorySummaryVO findSumRechargeOrderHistory(RechargeOrderHistoryQueryDTO dto);

  void rechReport(RechargeOrderHistoryQueryDTO dto, HttpServletRequest request, HttpServletResponse response);
}

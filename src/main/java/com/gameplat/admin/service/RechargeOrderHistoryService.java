package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.RechargeOrderHistory;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;

public interface RechargeOrderHistoryService extends IService<RechargeOrderHistory> {

  IPage<RechargeOrderHistoryVO> findPage(Page<RechargeOrderHistory> page,
      RechargeOrderHistoryQueryDTO dto);

  RechargeHistorySummaryVO findSumRechargeOrderHistory(RechargeOrderHistoryQueryDTO dto);

}

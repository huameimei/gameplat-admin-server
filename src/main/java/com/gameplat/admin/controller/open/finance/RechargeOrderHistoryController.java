package com.gameplat.admin.controller.open.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeHistorySummaryVO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.service.RechargeOrderHistoryService;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/rechargeOrderHistory")
public class RechargeOrderHistoryController {

  @Autowired private RechargeOrderHistoryService rechargeOrderHistoryService;

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:view')")
  public IPage<RechargeOrderHistoryVO> queryPage(
      Page<RechargeOrderHistory> page, RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findPage(page, dto);
  }

  @PostMapping("/statRechargeHisTotal")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:statRechargeHisTotal')")
  public RechargeHistorySummaryVO statRechargeHisTotal(RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findSumRechargeOrderHistory(dto);
  }

  @PostMapping("/statRechargeHisTotalYesterday")
  @PreAuthorize("hasAuthority('finance:rechargeOrderHistory:statRechargeHisTotalYesterday')")
  public RechargeHistorySummaryVO statRechargeHisTotalYesterday(RechargeOrderHistoryQueryDTO dto) {
    return rechargeOrderHistoryService.findSumRechargeOrderHistory(dto);
  }
}

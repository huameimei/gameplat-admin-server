package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LotteryRebateReportService;
import com.gameplat.model.entity.report.LotteryRebateReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KG新彩票代理返点
 *
 * @author cc
 */
@Tag(name = "KG新彩票代理返点")
@RestController
@RequestMapping("/api/admin/lottery/rebate")
public class LotteryRebateReportController {

  @Autowired private LotteryRebateReportService rebateReportService;

  @GetMapping(value = "/page")
  @Operation(summary = "代理返点分页列表")
  @PreAuthorize("hasAuthority('lottery:rebate:view')")
  public PageDtoVO<LotteryRebateReportVO> page(
      PageDTO<LotteryRebateReport> page, LotteryRebateReportDTO dto) {
    return rebateReportService.page(page, dto);
  }
}

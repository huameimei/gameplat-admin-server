package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.LotteryRebateReportDTO;
import com.gameplat.admin.model.vo.LotteryRebateReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LotteryRebateReportService;
import com.gameplat.model.entity.report.LotteryRebateReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @Description : KG新彩票代理返点 @Author : cc @Date : 2022/3/21 */
@Slf4j
@Api(tags = "KG新彩票代理返点")
@RestController
@RequestMapping("/api/admin/lottery/rebate")
public class LotteryRebateReportController {
  @Autowired private LotteryRebateReportService rebateReportService;

  /**
   * 代理返点分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @GetMapping(value = "/page")
  @ApiOperation(value = "代理返点分页列表")
  @PreAuthorize("hasAuthority('lottery:rebate:view')")
  public PageDtoVO<LotteryRebateReportVO> page(
      PageDTO<LotteryRebateReport> page, LotteryRebateReportDTO dto) {
    return rebateReportService.page(page, dto);
  }
}

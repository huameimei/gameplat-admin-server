package com.gameplat.admin.controller.open.report;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameFinancialReportQueryDTO;
import com.gameplat.admin.model.vo.GameFinancialReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameFinancialReportService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.model.entity.report.GameFinancialReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author aBen
 * @date 2022/3/6 17:24
 * @desc 游戏财务报表
 */
@Slf4j
@Api(tags = "财务报表")
@RestController
@RequestMapping("/api/admin/report/game/financial")
public class GameFinancialReportController {

  @Autowired private GameFinancialReportService gameFinancialReportService;

    @ApiOperation("查询财务报表")
    @GetMapping(value = "/findReportPage")
    @PreAuthorize("hasAuthority('financial:report:view')")
    public PageDtoVO<GameFinancialReportVO> findReportPage(Page<GameFinancialReport> page, GameFinancialReportQueryDTO queryDTO) {
        return gameFinancialReportService.findReportPage(page, queryDTO);
    }

    @ApiOperation("初始化财务报表")
    @GetMapping("/initReportList")
    @PreAuthorize("hasAuthority('financial:report:init')")
    @Log(module = ServiceName.ADMIN_SERVICE, desc = "初始化财务报表")
    public void initReportList(@RequestParam("statisticsTime") String statisticsTime) {
      try {
        if (StringUtils.isEmpty(statisticsTime)) {
            statisticsTime = DateUtil.format(new Date(), "yyyy-MM");
        }
        gameFinancialReportService.initGameFinancialReport(statisticsTime);
      } catch (Exception e) {
        log.info("初始化财务报表异常");
      }
    }

    @ApiOperation("导出财务报表")
    @GetMapping("/exportReport")
    @PreAuthorize("hasAuthority('financial:report:export')")
    @Log(module = ServiceName.ADMIN_SERVICE, desc = "导出财务报表")
    public void exportReport(@RequestParam("statisticsTime") String statisticsTime, HttpServletResponse response) {
        if (StringUtils.isEmpty(statisticsTime)) {
            statisticsTime = DateUtil.format(new Date(), "yyyy-MM");
        }
        gameFinancialReportService.exportGameFinancialReport(statisticsTime, response);
    }

}

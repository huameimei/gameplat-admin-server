package com.gameplat.admin.controller.open.report;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.dto.GameFinancialReportQueryDTO;
import com.gameplat.admin.model.vo.GameFinancialReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GameFinancialReportService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.report.GameFinancialReport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private GameFinancialReportService gameFinancialReportService;

    @ApiOperation("查询财务报表")
    @GetMapping(value = "/findReportPage")
    public PageDtoVO<GameFinancialReportVO> findReportPage(Page<GameFinancialReport> page, GameFinancialReportQueryDTO queryDTO) {
        return gameFinancialReportService.findReportPage(page, queryDTO);
    }

    @ApiOperation("初始化财务报表")
    @GetMapping("/initReportList")
    public void initReportList(@RequestParam("statisticsTime") String statisticsTime) {
        if (StringUtils.isEmpty(statisticsTime)) {
            statisticsTime = DateUtil.format(new Date(), "yyyy-MM");
        }
        gameFinancialReportService.initGameFinancialReport(statisticsTime);
    }

    @ApiOperation("导出财务报表")
    @GetMapping("/exportReport")
    public void exportReport(@RequestParam("statisticsTime") String statisticsTime, HttpServletResponse response) {
        if (StringUtils.isEmpty(statisticsTime)) {
            statisticsTime = DateUtil.format(new Date(), "yyyy-MM");
        }
        gameFinancialReportService.exportGameFinancialReport(statisticsTime, response);
    }

    @ApiOperation("总控拉取财务报表数据")
    @PostMapping("/masterPullReport")
    public List<GameFinancialReport> masterPullReport(String statisticsTime) {
        if (StringUtils.isEmpty(statisticsTime)) {
            throw new ServiceException("统计时间不能为空");
        }
        return gameFinancialReportService.masterPullReport(statisticsTime);
    }

}

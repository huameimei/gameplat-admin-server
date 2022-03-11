package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.dto.GameFinancialReportQueryDTO;
import com.gameplat.admin.model.vo.GameFinancialReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.report.GameFinancialReport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author aBen
 * @date 2022/3/6 17:24
 * @desc
 */
public interface GameFinancialReportService extends IService<GameFinancialReport> {

    List<GameFinancialReportVO> findGameFinancialReportList(GameFinancialReportQueryDTO dto);

    PageDtoVO<GameFinancialReportVO> findReportPage(Page<GameFinancialReport> page, GameFinancialReportQueryDTO queryDTO);

    void initGameFinancialReport(String statisticsTime);

    void exportGameFinancialReport(String statisticsTime, HttpServletResponse response);

}

package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameFinancialReportQueryDTO;
import com.gameplat.admin.model.vo.GameFinancialReportVO;
import com.gameplat.model.entity.report.GameFinancialReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aBen
 * @date 2022/3/6 17:50
 * @desc
 */
@Mapper
public interface GameFinancialReportMapper extends BaseMapper<GameFinancialReport> {

  List<GameFinancialReportVO> findGameFinancialReportList(GameFinancialReportQueryDTO dto);

  Page<GameFinancialReportVO> findGameFinancialReportPage(
          Page<GameFinancialReport> page, @Param("dto") GameFinancialReportQueryDTO dto);

  List<GameFinancialReport> initGameFinancialReport(
          @Param("statisticsTime") String statisticsTime,
          @Param("startTime") String startTime,
          @Param("endTime") String endTime,
          @Param("customerCode") String customerCode);

  BigDecimal findTotalLastWinAmount(GameFinancialReportQueryDTO dto);
}

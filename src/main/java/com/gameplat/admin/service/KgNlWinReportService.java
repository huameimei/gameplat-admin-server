package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlBetDailyDetailVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
public interface KgNlWinReportService {

  List<KgNlWinReportVO> findList(KgNlWinReportQueryDTO queryDTO);

  void exportReport(KgNlWinReportQueryDTO queryDTO, HttpServletResponse response);

  PageDtoVO<KgNlBetDailyDetailVO> findUserDetail(Page<KgNlBetDailyDetailVO> page, KgNlWinReportQueryDTO dto);

  void exportUserDetail(KgNlWinReportQueryDTO queryDTO, HttpServletResponse response);
}

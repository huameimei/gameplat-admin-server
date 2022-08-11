package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;

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
}

package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
public interface HgSportWinReportService {

  List<HgSportWinReportVO> findList(HgSportWinReportQueryDTO queryDTO);

  void exportReport(HgSportWinReportQueryDTO queryDTO, HttpServletResponse response);
}

package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.HgSportWinReportQueryDTO;
import com.gameplat.admin.model.vo.HgSportWinReportVO;

import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
public interface HgSportWinReportService {

  List<HgSportWinReportVO> findList(HgSportWinReportQueryDTO queryDTO);

  IPage<HgSportWinReportVO> findListByMemberNumber(Page<HgSportWinReportVO> page, HgSportWinReportQueryDTO queryDTO);

  void exportReport(HgSportWinReportQueryDTO queryDTO);
}

package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlBetDailyDetailVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import com.gameplat.model.entity.member.Member;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
public interface KgNlBetDailyDetailService extends IService<KgNlBetDailyDetail> {

  List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto);

  PageDtoVO<KgNlBetDailyDetailVO> findDetailPage(Page<KgNlBetDailyDetailVO> page, KgNlWinReportQueryDTO dto);

  void exportDetail(HttpServletResponse response, KgNlWinReportQueryDTO dto);

  void assembleKgNlBetDailyDetail(List<String> dayList, List<String> memberList, String platformCode);
}

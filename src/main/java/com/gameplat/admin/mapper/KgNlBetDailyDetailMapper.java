package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.dto.MemberBonusReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlBetDailyDetailVO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.model.vo.MemberBonusReportVO;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KgNlBetDailyDetailMapper extends BaseMapper<KgNlBetDailyDetail> {

  List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto);

  Page<KgNlBetDailyDetailVO> findDetailPage(Page<KgNlBetDailyDetailVO> page, @Param("dto") KgNlWinReportQueryDTO dto);

  List<KgNlBetDailyDetailVO> findDetailList(KgNlWinReportQueryDTO dto);

  KgNlBetDailyDetail findTotalData(KgNlWinReportQueryDTO dto);
}

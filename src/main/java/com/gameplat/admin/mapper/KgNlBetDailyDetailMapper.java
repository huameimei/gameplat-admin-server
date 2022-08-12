package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;

import java.util.List;

public interface KgNlBetDailyDetailMapper extends BaseMapper<KgNlBetDailyDetail> {

  List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto);
}

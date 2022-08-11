package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface KgNlBetDailyDetailMapper extends BaseMapper<KgNlBetDailyDetail> {

  List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto);
}

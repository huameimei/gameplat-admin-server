package com.gameplat.admin.mapper;

import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.datasource.mapper.ExtBaseMapper;
import com.gameplat.model.entity.game.GameRebateData;

import java.util.List;

public interface GameRebateDataMapper extends ExtBaseMapper<GameRebateData> {

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);
}

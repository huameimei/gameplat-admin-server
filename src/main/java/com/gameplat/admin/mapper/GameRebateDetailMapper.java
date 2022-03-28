package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;
import com.gameplat.model.entity.game.GameRebateDetail;

public interface GameRebateDetailMapper extends BaseMapper<GameRebateDetail> {

  GameRebateDetail queryAllGameRebateReport(GameRebateReportQueryDTO dto);
}

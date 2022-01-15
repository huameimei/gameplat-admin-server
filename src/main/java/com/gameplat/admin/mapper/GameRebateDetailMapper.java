package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.GameRebateDetail;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;

public interface GameRebateDetailMapper extends BaseMapper<GameRebateDetail> {

  GameRebateDetail queryAllGameRebateReport(GameRebateReportQueryDTO dto);
}

package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRebateStatisQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.model.entity.game.GameRebateReport;
import java.util.List;

public interface GameRebateReportMapper extends BaseMapper<GameRebateReport> {

  List<GameRebateReport> queryGameRebateReportByStatus(Long periodId, int status);

  List<GameReportVO> queryGameReport(GameRebateStatisQueryDTO dto);
}

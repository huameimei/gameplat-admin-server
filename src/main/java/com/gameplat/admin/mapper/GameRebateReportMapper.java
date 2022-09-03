package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRebateStatisQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.model.entity.game.GameRebateReport;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GameRebateReportMapper extends BaseMapper<GameRebateReport> {

  List<GameRebateReport> queryGameRebateReportByStatus(@Param("periodId") Long periodId, @Param("status") int status);

  List<GameReportVO> queryGameReport(GameRebateStatisQueryDTO dto);

  BigDecimal statisValidAmountByTime(
      @Param("statTime") String statTime,
      @Param("account") String account,
      @Param("gameKind") String gameKind,
      @Param("periodId") Long periodId);
}

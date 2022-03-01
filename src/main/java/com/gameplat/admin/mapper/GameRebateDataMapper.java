package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GameRebateDataMapper extends BaseMapper<GameRebateData> {

  List<GameRebateData> queryBetRecordList(GameRebateDataQueryDTO dto);

  int getDayCount(
      @Param("statTime") String statTime, @Param("gamePlatform") GamePlatform gamePlatform);

  int saveDayReport(
      @Param("statTime") String statTime,
      @Param("gamePlatform") GamePlatform gamePlatform,
      @Param("statTimeType") String statTimeType);

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);
}

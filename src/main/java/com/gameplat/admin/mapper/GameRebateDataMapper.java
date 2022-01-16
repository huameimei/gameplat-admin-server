package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameRebateData;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GameRebateDataMapper extends BaseMapper<GameRebateData> {

  List<GameRebateData> queryBetRecordList(GameRebateDataQueryDTO dto);

  int getDayCount(@Param("statTime")String statTime, @Param("gamePlatform") GamePlatform gamePlatform);

  int saveDayReport(@Param("statTime") String statTime,
      @Param("gamePlatform") GamePlatform gamePlatform,
      @Param("statTimeType")  String statTimeType);

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);
}

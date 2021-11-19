package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveRebateData;
import com.gameplat.admin.model.dto.LiveRebateDataQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LiveRebateDataMapper extends BaseMapper<LiveRebateData> {

  List<LiveRebateData> queryBetRecordList(LiveRebateDataQueryDTO dto);

  int getDayCount(@Param("statTime")String statTime, @Param("gamePlatform") GamePlatform gamePlatform);

  int saveDayReport(@Param("statTime") String statTime,
      @Param("gamePlatform") GamePlatform gamePlatform,
      @Param("statTimeType")  String statTimeType);

  List<LiveReportVO> queryLiveReport(LiveRebateDataQueryDTO dto);
}

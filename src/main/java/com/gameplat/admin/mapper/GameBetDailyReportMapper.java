package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameBetDailyReport;
import com.gameplat.admin.model.domain.GameRebatePeriod;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.GameMemberDayReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GameBetDailyReportMapper extends BaseMapper<GameBetDailyReport> {


  int getDayCount(@Param("statTime") String statTime,@Param("tableName") String tableName);

  int saveMemberDayReport(@Param("statTime") String statTime, @Param("gamePlatform") GamePlatform gamePlatform,@Param("tableName") String tableName);

  List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto);

  List<GameMemberDayReportVO> findByStatTimeBetweenAndValidBetAmountGtZero(@Param("liveRebatePeriod") GameRebatePeriod liveRebatePeriod,
      @Param("startDate") String startDate, @Param("endDate") String endDate);

  List<ActivityStatisticItem> getGameReportInfo(Map map);

  List<ActivityStatisticItem> findGameDmlDateList(Map map);
}

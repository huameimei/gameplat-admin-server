package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameBetDailyReport;
import com.gameplat.admin.model.domain.GameRebatePeriod;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.*;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface GameBetDailyReportMapper extends BaseMapper<GameBetDailyReport> {


  int getDayCount(@Param("statTime") String statTime,@Param("tableName") String tableName);


  List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto);


  Page<GameBetReportVO> querybetReportList(Page<GameBetDailyReportQueryDTO> page, @Param("dto") GameBetDailyReportQueryDTO dto);

  Map<String,Object> querySumReport(GameBetDailyReportQueryDTO dto);

  List<GameMemberDayReportVO> findByStatTimeBetweenAndValidBetAmountGtZero(@Param("liveRebatePeriod") GameRebatePeriod liveRebatePeriod,
      @Param("startDate") String startDate, @Param("endDate") String endDate);

  List<ActivityStatisticItem> getGameReportInfo(Map map);

  List<ActivityStatisticItem> findGameDmlDateList(Map map);

  /**
   * 插入投注日报表
   *
   * @param betDailyReport List
   */
  void insertGameBetDailyReport(@Param("list") List<GameBetDailyReport> betDailyReport);

  List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto);

  /**
   * 分组获取会员游戏报表
   * @param startDate
   * @param endDate
   * @return
   */
  List<DivideGameReportVO> findReportForDivide(@Param("startDate") String startDate,
                                               @Param("endDate") String endDate);
}

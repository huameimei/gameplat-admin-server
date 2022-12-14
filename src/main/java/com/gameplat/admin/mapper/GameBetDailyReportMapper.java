package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.*;
import com.gameplat.datasource.mapper.ExtBaseMapper;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.game.GameRebatePeriod;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GameBetDailyReportMapper extends ExtBaseMapper<GameBetDailyReport> {

  int getDayCount(@Param("statTime") String statTime, @Param("tableName") String tableName);

  List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto);

  Page<GameBetReportVO> querybetReportList(
      Page<GameBetDailyReportQueryDTO> page, @Param("dto") GameBetDailyReportQueryDTO dto);

  Map<String, Object> querySumReport(GameBetDailyReportQueryDTO dto);

  List<GameMemberDayReportVO> findByStatTimeBetweenAndValidBetAmountGtZero(
      @Param("gameRebatePeriod") GameRebatePeriod gameRebatePeriod,
      @Param("startDate") String startDate,
      @Param("endDate") String endDate);

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
   * 分组获取会员游戏报表--分红统计
   *
   * @param startDate
   * @param endDate
   * @return
   */
  List<DivideGameReportVO> findReportForDivide(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate,
      @Param("isIncludeAgent") Integer isIncludeAgent);

  /**
   * 分组获取会员游戏报表--工资统计
   *
   * @param startDate
   * @param endDate
   * @return
   */
  List<SalaryRechargeVO> findReportForSalary(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate,
      @Param("agentName") String agentName,
      @Param("isInclude") Integer isInclude);

  List<String> getSatisfyBetAccount(
          @Param("minBetAmount") String minBetAmount,
          @Param("startTime") String startTime,
          @Param("endTime") String endTime);

  /**
   * 获取达到有效投注金额的会员账号
   *
   * @param type
   * @param startTime
   * @param endTime
   * @return
   */
  List<String> getWealVipValid(@Param("type") Integer type,
                               @Param("startTime") String startTime,
                               @Param("endTime") String endTime);
}

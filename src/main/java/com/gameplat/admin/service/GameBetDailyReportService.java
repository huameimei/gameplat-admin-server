package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.GameBetDailyReportQueryDTO;
import com.gameplat.admin.model.vo.GameBetReportVO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.GameBetDailyReport;
import com.gameplat.model.entity.game.GamePlatform;

import java.util.List;
import java.util.Map;

public interface GameBetDailyReportService extends IService<GameBetDailyReport> {

  PageDtoVO queryPage(Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto);

  void saveGameBetDailyReport(String statTime, GamePlatform gamePlatform);

  List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto);

  PageDtoVO<GameBetReportVO> querybetReportList(
      Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto);

  /**
   * 查询游戏统计
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> getGameReportInfo(Map map);

  /**
   * 汇总投注日报
   *
   * @param list List
   */
  void assembleBetDailyReport(List<String> list);

  List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto);
}

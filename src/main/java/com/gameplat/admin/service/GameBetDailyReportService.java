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
import javax.servlet.http.HttpServletResponse;

public interface GameBetDailyReportService extends IService<GameBetDailyReport> {

  PageDtoVO<GameBetDailyReport> queryPage(
      Page<GameBetDailyReport> page, GameBetDailyReportQueryDTO dto);

  void saveGameBetDailyReport(String statTime, GamePlatform gamePlatform);

  List<GameReportVO> queryReportList(GameBetDailyReportQueryDTO dto);

  PageDtoVO<GameBetReportVO> queryBetReportList(
      Page<GameBetDailyReportQueryDTO> page, GameBetDailyReportQueryDTO dto);

  /**
   * 查询游戏统计
   *
   * @param map
   * @return
   */
  List<ActivityStatisticItem> getGameReportInfo(Map map);

  List<GameReportVO> queryGamePlatformReport(GameBetDailyReportQueryDTO dto);

  // 获取达到有效投注金额的会员账号
  List<String> getSatisfyBetAccount(String minBetAmount, String startTime, String endTime);

  List<String> getWealVipValid(Integer type, String startTime, String endTime);

  void exportGamePlatformReport(GameBetDailyReportQueryDTO dto, HttpServletResponse response);

  void exportGameBetDailyReport(HttpServletResponse response, GameBetDailyReportQueryDTO dto) throws Exception;

  void exportGameKindReport(GameBetDailyReportQueryDTO dto, HttpServletResponse response);
}

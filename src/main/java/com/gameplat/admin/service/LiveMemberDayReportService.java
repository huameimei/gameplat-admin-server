package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import java.util.List;
import java.util.Map;

public interface LiveMemberDayReportService  extends IService<LiveMemberDayReport> {

  PageDtoVO queryPage(Page<LiveMemberDayReport> page, LiveMemberDayReportQueryDTO dto);

  void saveMemberDayReport(String statTime, GamePlatform gamePlatform);

  List<LiveReportVO> queryReportList(LiveMemberDayReportQueryDTO dto);

  /**
   *查询游戏统计
   * @param map
   * @return
   */
  List<ActivityStatisticItem> getGameReportInfo(Map map);
}

package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.LiveMemberDayReportService;
import com.gameplat.common.exception.ServiceException;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/live/liveMemberDayReport")
public class LiveMemberUserReportController {

  @Autowired
  private LiveMemberDayReportService liveMemberDayReportService;

  @Resource
  private GamePlatformService gamePlatformService;


  @GetMapping(value = "queryPage")
  public PageDtoVO<LiveMemberDayReport> queryPage(Page<LiveMemberDayReport> page,LiveMemberDayReportQueryDTO dto) {
    return liveMemberDayReportService.queryPage(page, dto);
  }


  // TODO 导出真人投注日报表


  /**
   * 真人重新生成日报表
   * @param statTime
   * @param gameCode
   * @throws ServiceException
   */
  @GetMapping(value = "resetDayReport")
  //@Log(type = LogType.LIVEREBATE, content = "'真人重新生成日报表,时间：'+#statTime+',类型：'+T(com.cz.gameplat.log.LogUtil).liveTypeName(#gameCode)")
  public void resetDayReport(String statTime,String gameCode) throws ServiceException {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform = list.stream().filter(item ->  item.getCode().equals(gameCode)).findAny().orElse(null);
    if(gamePlatform == null){
      throw new ServiceException("游戏类型不存在！");
    }
    liveMemberDayReportService.saveMemberDayReport(statTime, gamePlatform);
  }


  /**
   * 查询真人数据统计
   */
  @GetMapping(value = "queryReport")
  public List<LiveReportVO> queryReport(LiveMemberDayReportQueryDTO dto) {
    return liveMemberDayReportService.queryReportList(dto);
  }

  // TODO 导出真人投注报表
}

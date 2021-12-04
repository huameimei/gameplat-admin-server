package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveMemberDayReport;
import com.gameplat.admin.model.dto.LiveMemberDayReportQueryDTO;
import com.gameplat.admin.model.dto.OperLiveMemberDayReportDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.LiveMemberDayReportService;
import com.gameplat.base.common.exception.ServiceException;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
   */
  @PostMapping(value = "resetDayReport")
  public void resetDayReport(@RequestBody OperLiveMemberDayReportDTO dto) throws ServiceException {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform = list.stream().filter(item ->  item.getCode().equals(dto.getPlatformCode())).findAny().orElse(null);
    if(gamePlatform == null){
      throw new ServiceException("游戏类型不存在！");
    }
    liveMemberDayReportService.saveMemberDayReport(dto.getStatTime(), gamePlatform);
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

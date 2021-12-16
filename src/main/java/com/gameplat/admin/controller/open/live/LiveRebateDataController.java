package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveRebateData;
import com.gameplat.admin.model.dto.LiveRebateDataQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.LiveRebateDataService;
import java.rmi.ServerException;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/liveRebateData")
public class LiveRebateDataController {

  @Resource
  private LiveRebateDataService liveRebateDataService;

  @Resource
  private GamePlatformService gamePlatformService;



  @GetMapping(value = "queryPage")
  public PageDtoVO<LiveRebateData> queryPage(Page<LiveRebateData> page, LiveRebateDataQueryDTO dto) throws Exception {
    return liveRebateDataService.queryPageData(page, dto);
  }


  @GetMapping(value = "resetDayReport")
  //@Log(type = LogType.LIVEREBATE, content = "'真人重新生成日报表,时间：'+#statTime+',类型：'+T(com.cz.gameplat.log.LogUtil).liveTypeName(#gameCode)")
  public void resetDayReport(String statTime,String gameCode) throws ServerException {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform = list.stream().filter(item ->  item.getCode().equals(gameCode)).findAny().orElse(null);
    if(gamePlatform == null){
      throw new ServerException("游戏类型不存在！");
    }
    liveRebateDataService.saveRebateReport(statTime, gamePlatform);
  }

  @GetMapping(value = "queryReport")
  public List<LiveReportVO> queryLiveReport(LiveRebateDataQueryDTO dto){
    return liveRebateDataService.queryLiveReport(dto);
  }

   // TODO 导出真人投注日报表
   // TODO 导出真人投注报表

}

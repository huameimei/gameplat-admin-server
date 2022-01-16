package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameRebateData;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameRebateDataService;
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
@RequestMapping("/api/admin/game/gameRebateData")
public class GameRebateDataController {

  @Resource
  private GameRebateDataService gameRebateDataService;

  @Resource
  private GamePlatformService gamePlatformService;

  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameRebateData> queryPage(Page<GameRebateData> page, GameRebateDataQueryDTO dto) throws Exception {
    return gameRebateDataService.queryPageData(page, dto);
  }


  /**
   * 真人重新生成日报表
   * @param statTime
   * @param gameCode
   * @throws ServerException
   */
  @GetMapping(value = "/resetDayReport")
  public void resetDayReport(String statTime,String gameCode) throws ServerException {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform = list.stream().filter(item ->  item.getCode().equals(gameCode)).findAny().orElse(null);
    if(gamePlatform == null){
      throw new ServerException("游戏类型不存在！");
    }
    gameRebateDataService.saveRebateReport(statTime, gamePlatform);
  }

  @GetMapping(value = "/queryReport")
  public List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto){
    return gameRebateDataService.queryGameReport(dto);
  }

   // TODO 导出真人投注日报表
   // TODO 导出真人投注报表

}

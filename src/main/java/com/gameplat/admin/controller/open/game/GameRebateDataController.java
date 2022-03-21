package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.dto.OperGameRebateDataDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameRebateDataService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameRebateData")
public class GameRebateDataController {

  @Autowired private GameRebateDataService gameRebateDataService;

  @Autowired private GamePlatformService gamePlatformService;

  @SneakyThrows
  @GetMapping(value = "/queryPage")
  public PageDtoVO<GameRebateData> queryPage(
      Page<GameRebateData> page, GameRebateDataQueryDTO dto) {
    return gameRebateDataService.queryPageData(page, dto);
  }

  /** 游戏重新生成交收日报表 */
  @PostMapping(value = "/resetDayReport")
  public void resetDayReport(@RequestBody OperGameRebateDataDTO dto) {
    List<GamePlatform> list = gamePlatformService.list();
    GamePlatform gamePlatform =
        list.stream()
            .filter(item -> item.getCode().equals(dto.getPlatformCode()))
            .findAny()
            .orElse(null);
    Assert.isNull(gamePlatform, "游戏平台不存在！");
    gameRebateDataService.saveRebateDayReport(dto.getStatTime(), gamePlatform);
  }

  @GetMapping(value = "/queryReport")
  public List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto) {
    return gameRebateDataService.queryGameReport(dto);
  }

  // TODO 导出真人投注日报表
  // TODO 导出真人投注报表

}

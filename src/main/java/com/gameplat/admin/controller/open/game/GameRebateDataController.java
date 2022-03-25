package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.GamePlatformService;
import com.gameplat.admin.service.GameRebateDataService;
import com.gameplat.model.entity.game.GameRebateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/game/gameRebateData")
public class GameRebateDataController {

  @Autowired private GameRebateDataService gameRebateDataService;

  @Autowired private GamePlatformService gamePlatformService;

  @GetMapping(value = "/queryPage")
  @PreAuthorize("hasAuthority('game:gameRebateData:list')")
  public PageDtoVO<GameRebateData> queryPage(
      Page<GameRebateData> page, GameRebateDataQueryDTO dto) {
    return gameRebateDataService.queryPageData(page, dto);
  }
}

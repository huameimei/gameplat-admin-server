package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.admin.service.GameRebateConfigService;
import com.gameplat.model.entity.game.GameRebateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/game/gameRebateConfig")
public class GameRebateConfigController {

  @Autowired private GameRebateConfigService gameRebateConfigService;

  @GetMapping(value = "/queryAll/{userLevel}")
  public List<GameRebateConfig> queryAll(
      @PathVariable(value = "userLevel", required = false) String userLevel) {
    return gameRebateConfigService.queryAll(userLevel);
  }

  @GetMapping(value = "/getById/{id}")
  public GameRebateConfig getById(@PathVariable("id") Long id) {
    return gameRebateConfigService.getById(id);
  }

  @PostMapping(value = "/add")
  public void add(@RequestBody OperGameRebateConfigDTO dto) {
    gameRebateConfigService.addGameRebateConfig(dto);
  }

  @PutMapping(value = "/update")
  public void update(@RequestBody OperGameRebateConfigDTO dto) {
    gameRebateConfigService.updateGameRebateConfig(dto);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id) {
    gameRebateConfigService.delete(id);
  }
}

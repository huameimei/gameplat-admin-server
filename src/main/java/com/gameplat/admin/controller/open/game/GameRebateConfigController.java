package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.domain.GameRebateConfig;
import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.admin.service.GameRebateConfigService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/gameRebateConfig")
public class GameRebateConfigController {

  @Autowired
  private GameRebateConfigService gameRebateConfigService;

  @GetMapping(value = "/queryAll/{userLevel}")
  public List<GameRebateConfig> queryAll(@PathVariable(value = "userLevel",required = false)  String userLevel) {
    return gameRebateConfigService.queryAll(userLevel);
  }

  @GetMapping(value = "/getById/{id}")
  public GameRebateConfig getById(@PathVariable("id") Long id){
    return gameRebateConfigService.getById(id);
  }

  @PostMapping(value = "/add")
  public void add(@RequestBody OperGameRebateConfigDTO dto) {
    gameRebateConfigService.addGameRebateConfig(dto);
  }

  @PutMapping(value = "/update")
  public void update(@RequestBody OperGameRebateConfigDTO dto){
    gameRebateConfigService.updateGameRebateConfig(dto);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id){
    gameRebateConfigService.delete(id);
  }
}

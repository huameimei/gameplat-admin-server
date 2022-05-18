package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.admin.service.GameRebateConfigService;
import com.gameplat.model.entity.game.GameRebateConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "游戏返水配置")
@RestController
@RequestMapping("/api/admin/game/gameRebateConfig")
public class GameRebateConfigController {

  @Autowired private GameRebateConfigService gameRebateConfigService;

  @Operation(summary = "根据会员层级查询")
  @GetMapping(value = "/queryAll/{userLevel}")
  @PreAuthorize("hasAuthority('game:gameRebateConfig:view')")
  public List<GameRebateConfig> queryAll(
      @PathVariable(value = "userLevel", required = false) String userLevel) {
    return gameRebateConfigService.queryAll(userLevel);
  }

  @Operation(summary = "根据ID查询详情")
  @GetMapping(value = "/getById/{id}")
  public GameRebateConfig getById(@PathVariable("id") Long id) {
    return gameRebateConfigService.getById(id);
  }

  @Operation(summary = "添加")
  @PostMapping(value = "/add")
  @PreAuthorize("hasAuthority('game:gameRebateConfig:add')")
  public void add(@RequestBody OperGameRebateConfigDTO dto) {
    gameRebateConfigService.addGameRebateConfig(dto);
  }

  @Operation(summary = "修改")
  @PostMapping(value = "/update")
  @PreAuthorize("hasAuthority('game:gameRebateConfig:update')")
  public void update(@RequestBody OperGameRebateConfigDTO dto) {
    gameRebateConfigService.updateGameRebateConfig(dto);
  }

  @Operation(summary = "根据ID删除")
  @PostMapping(value = "/delete/{id}")
  @PreAuthorize("hasAuthority('game:gameRebateConfig:remove')")
  public void delete(@PathVariable("id") Long id) {
    gameRebateConfigService.delete(id);
  }
}

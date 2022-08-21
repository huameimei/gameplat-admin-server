package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "游戏导航")
@RestController
@RequestMapping("/api/admin/game/bar")
public class GameBarController {

  @Autowired private GameBarService service;

  @Operation(summary = "获取游戏导航列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:bar:view')")
  public List<GameBarVO> gameBarList(GameBarDTO dto) {
    return service.gameBarList(dto);
  }

  @Operation(summary = "编辑游戏导航列表")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('game:bar:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏导航-->编辑游戏导航列表' + #dto")
  public void editGameBar(@RequestBody GameBarDTO dto) {
    service.editGameBar(dto);
  }

  @Operation(summary = "移除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('game:bar:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏导航-->移除:' + #id")
  public void deleteGameBar(@RequestParam Long id) {
    service.deleteGameBar(id);
  }

  @Operation(summary = "设为热门")
  @GetMapping("setHot")
  @PreAuthorize("hasAuthority('game:bar:setHot')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏导航-->设为热门:' + #id")
  public void setHot(@RequestParam Long id) {
    service.setHot(id);
  }
}

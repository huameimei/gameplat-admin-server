package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
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
  public void editGameBar(@RequestBody GameBarDTO dto) {
    service.editGameBar(dto);
  }

  @Operation(summary = "移除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('game:bar:remove')")
  public void deleteGameBar(@RequestParam Long id) {
    service.deleteGameBar(id);
  }

  @Operation(summary = "设为热门")
  @GetMapping("setHot")
  @PreAuthorize("hasAuthority('game:bar:setHot')")
  public void setHot(@RequestParam Long id) {
    service.setHot(id);
  }
}

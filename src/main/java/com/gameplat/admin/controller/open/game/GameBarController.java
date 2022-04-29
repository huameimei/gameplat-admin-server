package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "游戏导航")
@RestController
@RequestMapping("/api/admin/game/bar")
public class GameBarController {

  @Autowired private GameBarService service;

  @ApiOperation("获取游戏导航列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:bar:view')")
  public List<GameBarVO> gameBarList(GameBarDTO dto) {
    return service.gameBarList(dto);
  }

  @ApiOperation("编辑游戏导航列表")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('game:bar:edit')")
  public void editGameBar(@RequestBody GameBarDTO dto) {
    service.editGameBar(dto);
  }

  @ApiOperation("移除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('game:bar:remove')")
  public void deleteGameBar(@RequestParam Long id) {
    service.deleteGameBar(id);
  }

  @ApiOperation("设为热门")
  @GetMapping("setHot")
  @PreAuthorize("hasAuthority('game:bar:setHot')")
  public void setHot(@RequestParam Long id) {
    service.setHot(id);
  }
}

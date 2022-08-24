package com.gameplat.admin.controller.open.game;

import com.gameplat.admin.model.dto.GameBarNewDTO;
import com.gameplat.admin.model.vo.GameBarNewVO;
import com.gameplat.admin.service.GameBarNewService;
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
@RequestMapping("/api/admin/game/bar/new")
public class GameBarNewController {

  @Autowired private GameBarNewService service;

  @Operation(summary = "获取游戏导航列表")
  @GetMapping("/list")
//  @PreAuthorize("hasAuthority('game:bar:new:view')")
  public List<GameBarNewVO> gameBarList() {
    return service.gameBarNewList();
  }

  @Operation(summary = "编辑游戏导航列表")
  @PostMapping("/edit")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏导航-->编辑游戏导航列表:' + #dto")
  public void editGameBar(@RequestBody GameBarNewDTO dto) {
    service.editGameBarNew(dto);
  }

  @Operation(summary = "批量设置返水，批量设置游戏是否展示")
  @PostMapping("/editData")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏导航-->批量设置返水，批量设置游戏是否展示:' + #dto")
  public void editIsWater(@RequestBody GameBarNewDTO dto) {
    service.editIsWater(dto);
  }

//  @Operation(summary = "移除")
//  @PostMapping("/delete")
//  @PreAuthorize("hasAuthority('game:bar:remove')")
//  public void deleteGameBar(@RequestParam Long id) {
//    service.deleteGameBar(id);
//  }
//
//  @Operation(summary = "设为热门")
//  @GetMapping("setHot")
//  @PreAuthorize("hasAuthority('game:bar:setHot')")
//  public void setHot(@RequestParam Long id) {
//    service.setHot(id);
//  }
}

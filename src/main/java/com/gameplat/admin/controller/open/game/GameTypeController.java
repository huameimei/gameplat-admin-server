package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameTypeQueryDTO;
import com.gameplat.admin.model.dto.OperGameTypeDTO;
import com.gameplat.admin.model.vo.GameTypeVO;
import com.gameplat.admin.service.GameTypeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GameType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "游戏类型")
@RestController
@RequestMapping("/api/admin/game/gameType")
public class GameTypeController {

  @Autowired private GameTypeService gameTypeService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gameType:view')")
  public IPage<GameTypeVO> selectGameTypeList(PageDTO<GameType> page, GameTypeQueryDTO dto) {
    return gameTypeService.selectGameTypeList(page, dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('game:gameType:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏类型-->编辑:' + #dto" )
  public void updateGameType(@RequestBody OperGameTypeDTO dto) {
    gameTypeService.updateGameType(dto);
  }

  @Operation(summary = "获取全部")
  @GetMapping(value = "/queryGameTypeList")
  public List<GameType> queryGameTypeList() {
    return Optional.ofNullable(gameTypeService.query().list()).orElse(Collections.emptyList());
  }
}

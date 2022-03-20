package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameTypeQueryDTO;
import com.gameplat.admin.model.dto.OperGameTypeDTO;
import com.gameplat.admin.model.vo.GameTypeVO;
import com.gameplat.admin.service.GameTypeService;
import com.gameplat.model.entity.game.GameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/game/gameType")
public class GameTypeController {

  @Autowired private GameTypeService gameTypeService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:gameType:list')")
  public IPage<GameTypeVO> selectGameTypeList(PageDTO<GameType> page, GameTypeQueryDTO dto) {
    return gameTypeService.selectGameTypeList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('game:gameType:edit')")
  public void updateGameType(@RequestBody OperGameTypeDTO dto) {
    gameTypeService.updateGameType(dto);
  }

  @GetMapping(value = "/queryGameTypeList")
  public List<GameType> queryGameTypeList() {
    return Optional.ofNullable(gameTypeService.query().list()).orElse(Collections.emptyList());
  }
}

package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.model.vo.GameVO;
import com.gameplat.admin.service.GameService;
import com.gameplat.model.entity.game.Game;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "游戏")
@RestController
@RequestMapping("/api/admin/game/game")
public class GameController {

  @Autowired private GameService gameService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:game:view')")
  public IPage<GameVO> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return gameService.selectGameList(page, dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('game:game:edit')")
  public void updateGame(@RequestBody OperGameDTO operGameDTO) {
    gameService.updateGame(operGameDTO);
  }
}

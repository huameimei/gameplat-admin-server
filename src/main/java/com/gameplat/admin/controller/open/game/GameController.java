package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.model.vo.GameVO;
import com.gameplat.admin.service.GameService;
import com.gameplat.model.entity.game.Game;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "游戏")
@RestController
@RequestMapping("/api/admin/game/game")
public class GameController {

  @Autowired private GameService gameService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:game:view')")
  public IPage<GameVO> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return gameService.selectGameList(page, dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('game:game:edit')")
  public void updateGame(@RequestBody OperGameDTO operGameDTO) {
    gameService.updateGame(operGameDTO);
  }
}

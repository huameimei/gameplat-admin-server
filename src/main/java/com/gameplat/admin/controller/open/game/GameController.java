package com.gameplat.admin.controller.open.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/game/game")
public class GameController {

  @Autowired private GameService gameService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('game:game:list')")
  public IPage<Game> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return gameService.selectGameList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('game:game:edit')")
  public void updateGame(@RequestBody OperGameDTO operGameDTO) {
    gameService.updateGame(operGameDTO);
  }
}

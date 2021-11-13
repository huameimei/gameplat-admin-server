package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Game;
import com.gameplat.admin.model.dto.GameQueryDTO;
import com.gameplat.admin.model.dto.OperGameDTO;
import com.gameplat.admin.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/game")
public class GameController {

  @Autowired private GameService gameService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('live:game:list')")
  public IPage<Game> selectGameList(PageDTO<Game> page, GameQueryDTO dto) {
    return gameService.selectGameList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('live:game:edit')")
  public void updateGame(@RequestBody OperGameDTO operGameDTO) {
    gameService.updateGame(operGameDTO);
  }
}

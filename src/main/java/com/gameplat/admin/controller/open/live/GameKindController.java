package com.gameplat.admin.controller.open.live;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.service.GameKindService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/live/gameKind/")
public class GameKindController {

  @Autowired private GameKindService gameKindService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('live:gameKind:list')")
  public IPage<GameKind> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto) {
    return gameKindService.selectGameKindList(page, dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('live:gameKind:edit')")
  public void updateGameKind(@RequestBody OperGameKindDTO operGameKindDTO) {
    gameKindService.updateGameKind(operGameKindDTO);
  }

  @GetMapping(value = "queryLiveGameKindList")
  public List<GameKind> queryLiveGameKindList() {
    return Optional.ofNullable(gameKindService.query().list()).orElse(Collections.emptyList());
  }
}

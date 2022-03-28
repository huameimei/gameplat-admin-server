package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import com.gameplat.admin.service.GameBlacklistService;
import com.gameplat.model.entity.game.GameBlacklist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/account/gameBlack")
public class OpenGameBlacklistController {

  @Autowired private GameBlacklistService gameBlacklistService;

  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:gameBlack:remove')")
  public void delete(@PathVariable Long id) {
    gameBlacklistService.delete(id);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:gameBlack:view')")
  public IPage<GameBlacklist> findAccountBlacklist(
      PageDTO<GameBlacklist> page, GameBlacklistQueryDTO gameBlacklistQueryDTO) {
    return gameBlacklistService.queryGameBlacklistList(page, gameBlacklistQueryDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:gameBlack:add')")
  public void add(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.save(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:gameBlack:edit')")
  public void edit(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.update(dto);
  }
}

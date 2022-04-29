package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import com.gameplat.admin.service.GameBlacklistService;
import com.gameplat.model.entity.game.GameBlacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "游戏黑名单")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/gameBlack")
public class OpenGameBlacklistController {

  @Autowired private GameBlacklistService gameBlacklistService;

  @ApiOperation("删除")
  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:gameBlack:remove')")
  public void delete(@PathVariable Long id) {
    gameBlacklistService.delete(id);
  }

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:gameBlack:view')")
  public IPage<GameBlacklist> findAccountBlacklist(
      PageDTO<GameBlacklist> page, GameBlacklistQueryDTO dto) {
    return gameBlacklistService.queryGameBlacklistList(page, dto);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:gameBlack:add')")
  public void add(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.save(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:gameBlack:edit')")
  public void edit(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.update(dto);
  }
}

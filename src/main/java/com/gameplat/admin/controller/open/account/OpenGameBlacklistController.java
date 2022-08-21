package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.GameBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperGameBlacklistDTO;
import com.gameplat.admin.service.GameBlacklistService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.game.GameBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "游戏黑名单")
@RestController
@RequestMapping("/api/admin/account/gameBlack")
public class OpenGameBlacklistController {

  @Autowired private GameBlacklistService gameBlacklistService;

  @Operation(summary = "删除")
  @PostMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:gameBlack:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏黑名单->删除id:' + #id "
  )
  public void delete(@PathVariable Long id) {
    gameBlacklistService.delete(id);
  }

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:gameBlack:view')")
  public IPage<GameBlacklist> findAccountBlacklist(
      PageDTO<GameBlacklist> page, GameBlacklistQueryDTO dto) {
    return gameBlacklistService.queryGameBlacklistList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:gameBlack:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏黑名单->添加target:' + #dto.target + 'blackType:' + #dto.blackType + 'liveCategory:' + #dto.liveCategory"
  )
  public void add(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.save(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:gameBlack:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'游戏黑名单->编辑id:' + #dto.id + 'target:' + #dto.target + 'blackType:' " +
      "+ #dto.blackType + 'liveCategory:' + #dto.liveCategory"
  )
  public void edit(@RequestBody OperGameBlacklistDTO dto) {
    gameBlacklistService.update(dto);
  }
}

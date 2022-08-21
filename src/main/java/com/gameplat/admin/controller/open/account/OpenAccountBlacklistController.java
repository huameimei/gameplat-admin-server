package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.UserBlackGames;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.blacklist.AccountBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "账号黑名单管理")
@RestController
@RequestMapping("/api/admin/account/accountBlack")
public class OpenAccountBlacklistController {

  @Autowired private AccountBlacklistService accountBlacklistService;

  @Operation(summary = "删除")
  @PostMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:accountBlack:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'账号黑名单管理->删除id:' + #id")
  public void delete(@PathVariable Long id) {
    accountBlacklistService.delete(id);
  }

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:accountBlack:view')")
  public IPage<AccountBlacklist> findList(
      PageDTO<AccountBlacklist> page, AccountBlacklistQueryDTO dto) {
    return accountBlacklistService.selectAccountBlacklistList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:accountBlack:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'账号黑名单管理->添加:' + #dto.account + 'ip:' + #dto.ip + 'games:' + #dto.games")
  public void add(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.save(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:accountBlack:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'账号黑名单管理->编辑:' + #dto.account + 'ip:' + #dto.ip + 'games:' + #dto.games")
  public void edit(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.update(dto);
  }

  @Operation(summary = "获取黑名单类型")
  @GetMapping("/getAccountBlackTypeList")
  public List<Object> getGameEnums() {
    return UserBlackGames.getEnum();
  }
}

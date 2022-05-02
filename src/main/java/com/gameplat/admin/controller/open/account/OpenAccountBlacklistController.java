package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import com.gameplat.common.enums.UserBlackGames;
import com.gameplat.model.entity.blacklist.AccountBlacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "账号黑名单管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/accountBlack")
public class OpenAccountBlacklistController {

  @Autowired private AccountBlacklistService accountBlacklistService;

  @ApiOperation("删除")
  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:accountBlack:remove')")
  public void delete(@PathVariable Long id) {
    accountBlacklistService.delete(id);
  }

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:accountBlack:view')")
  public IPage<AccountBlacklist> findList(
      PageDTO<AccountBlacklist> page, AccountBlacklistQueryDTO dto) {
    return accountBlacklistService.selectAccountBlacklistList(page, dto);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:accountBlack:add')")
  public void add(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.save(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:accountBlack:edit')")
  public void edit(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.update(dto);
  }

  @ApiOperation("获取黑名单类型")
  @GetMapping("/getAccountBlackTypeList")
  public List<Object> getGameEnums() {
    return UserBlackGames.getEnum();
  }
}

package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import com.gameplat.common.enums.UserBlackGames;
import com.gameplat.model.entity.blacklist.AccountBlacklist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/account/accountBlack")
public class OpenAccountBlacklistController {

  @Autowired private AccountBlacklistService accountBlacklistService;

  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:accountBlack:del')")
  public void delete(@PathVariable Long id) {
    accountBlacklistService.delete(id);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:accountBlack:list')")
  public IPage<AccountBlacklist> findAccountBlacklist(
      PageDTO<AccountBlacklist> page, AccountBlacklistQueryDTO accountBlacklistQueryDTO) {
    return accountBlacklistService.selectAccountBlacklistList(page, accountBlacklistQueryDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:accountBlack:add')")
  public void add(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.save(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:accountBlack:edit')")
  public void edit(@RequestBody OperAccountBlacklistDTO dto) {
    accountBlacklistService.update(dto);
  }

  @GetMapping("/getAccountBlackTypeList")
  public List<Object> getGameEnums() {
    return UserBlackGames.getEnum();
  }
}

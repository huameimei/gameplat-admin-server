package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.common.enums.UserBlackGames;
import com.gameplat.admin.model.domain.AccountBlacklist;
import com.gameplat.admin.model.dto.AccountBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperAccountBlacklistDTO;
import com.gameplat.admin.service.AccountBlacklistService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  /** 新增会员ip黑名单信息 */
  @GetMapping("/getAccountBlackTypeList")
  public List<Map> getGameEnums() {
    return UserBlackGames.getEnum();
  }
}

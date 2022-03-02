package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.admin.service.BankBlacklistService;
import com.gameplat.model.entity.blacklist.BankBlacklist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/account/bankBlack")
public class OpenBankBlacklistController {

  @Autowired private BankBlacklistService bankBlacklistService;

  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bankBlack:del')")
  public void delete(@PathVariable Long id) {
    bankBlacklistService.delete(id);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bankBlack:list')")
  public IPage<BankBlacklist> findBankBlacklist(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO bankBlacklistQueryDTO) {
    return bankBlacklistService.queryBankBlacklistList(page, bankBlacklistQueryDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bankBlack:add')")
  public void add(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.save(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:bankBlack:edit')")
  public void edit(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.update(dto);
  }
}

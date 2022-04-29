package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.admin.service.BankBlacklistService;
import com.gameplat.model.entity.blacklist.BankBlacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "银行卡黑名单")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/bankBlack")
public class OpenBankBlacklistController {

  @Autowired private BankBlacklistService bankBlacklistService;

  @ApiOperation("删除")
  @DeleteMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bankBlack:remove')")
  public void delete(@PathVariable Long id) {
    bankBlacklistService.delete(id);
  }

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bankBlack:view')")
  public IPage<BankBlacklist> findBankBlacklist(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO dto) {
    return bankBlacklistService.queryBankBlacklistList(page, dto);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bankBlack:add')")
  public void add(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.save(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('account:bankBlack:edit')")
  public void edit(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.update(dto);
  }
}

package com.gameplat.admin.controller.open.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.BankBlacklistQueryDTO;
import com.gameplat.admin.model.dto.OperBankBlacklistDTO;
import com.gameplat.admin.service.BankBlacklistService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.blacklist.BankBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "银行卡黑名单")
@Slf4j
@RestController
@RequestMapping("/api/admin/account/bankBlack")
public class OpenBankBlacklistController {

  @Autowired private BankBlacklistService bankBlacklistService;

  @Operation(summary = "删除")
  @PostMapping("/del/{id}")
  @PreAuthorize("hasAuthority('account:bankBlack:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'银行卡黑名单->删除id:' + #id")
  public void delete(@PathVariable Long id) {
    bankBlacklistService.delete(id);
  }

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:bankBlack:view')")
  public IPage<BankBlacklist> findBankBlacklist(
      PageDTO<BankBlacklist> page, BankBlacklistQueryDTO dto) {
    return bankBlacklistService.queryBankBlacklistList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:bankBlack:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'银行卡黑名单->添加cardNo:' + #dto.cardNo + 'bankName:' + #dto.bankName + 'blackType:' + #dto.blackType" +
      "+ 'remarks:' + #dto.remarks")
  public void add(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.save(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('account:bankBlack:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type = LogType.ADMIN,
    desc = "'银行卡黑名单->编辑id:' + #dto.id +  'cardNo:' + #dto.cardNo + 'bankName:' + #dto.bankName + 'blackType:' + #dto.blackType" +
      "+ 'remarks:' + #dto.remarks")
  public void edit(@RequestBody OperBankBlacklistDTO dto) {
    bankBlacklistService.update(dto);
  }
}

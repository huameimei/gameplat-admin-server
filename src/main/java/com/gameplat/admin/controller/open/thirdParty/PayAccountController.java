package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.admin.service.PayAccountService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.PayAccountTypeEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.PayAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/thirdParty/payAccount")
public class PayAccountController {

  @Autowired private PayAccountService payAccountService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除收款值账户：' + #id")
  public void remove(@PathVariable Long id) {
    payAccountService.delete(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'新增收款值账户id=' + #dto.id")
  public void add(@RequestBody PayAccountAddDTO dto) {
    dto.setType(PayAccountTypeEnum.ORDINARY_ACCOUNT.getValue());
    payAccountService.save(dto);
  }

  @PostMapping("/addVip")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增VIP收款值账户id=' + #dto.id")
  public void addVip(@RequestBody PayAccountAddDTO dto) {
    dto.setType(PayAccountTypeEnum.VIP_ACCOUNT.getValue());
    payAccountService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改收款值账户id=' + #dto.id")
  public void edit(@RequestBody PayAccountEditDTO dto) {
    payAccountService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改收款值账户状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    payAccountService.updateStatus(id, status);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:page')")
  public IPage<PayAccountVO> queryPage(Page<PayAccount> page, PayAccountQueryDTO dto) {
    return payAccountService.findPayAccountPage(page, dto);
  }

  @GetMapping("/get")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:get')")
  public PayAccount get(Long id) {
    return payAccountService.getById(id);
  }

  @GetMapping("/queryAccounts")
//  @PreAuthorize("hasAuthority('thirdParty:payAccount:queryAccounts')")
  public List<String> queryAccounts() {
    return payAccountService.queryAccounts();
  }
}

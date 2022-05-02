package com.gameplat.admin.controller.open.payment;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "收款账户管理")
@RestController
@RequestMapping("/api/admin/thirdParty/payAccount")
public class PayAccountController {

  @Autowired private PayAccountService payAccountService;

  @ApiOperation("删除")
  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除收款值账户：' + #id")
  public void remove(@PathVariable Long id) {
    payAccountService.delete(id);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'新增收款值账户id=' + #dto.id")
  public void add(@RequestBody PayAccountAddDTO dto) {
    dto.setType(PayAccountTypeEnum.ORDINARY_ACCOUNT.getValue());
    payAccountService.save(dto);
  }

  @ApiOperation("添加VIP收款账户")
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

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改收款值账户id=' + #dto.id")
  public void edit(@RequestBody PayAccountEditDTO dto) {
    payAccountService.update(dto);
  }

  @ApiOperation("修改状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改收款值账户状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    payAccountService.updateStatus(id, status);
  }

  @ApiOperation("查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:view')")
  public IPage<PayAccountVO> queryPage(Page<PayAccount> page, PayAccountQueryDTO dto) {
    return payAccountService.findPayAccountPage(page, dto);
  }

  @ApiOperation("根据ID获取")
  @GetMapping("/get")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:get')")
  public PayAccount get(Long id) {
    return payAccountService.getById(id);
  }

  @ApiOperation("获取全部")
  @GetMapping("/queryAccounts")
  //  @PreAuthorize("hasAuthority('thirdParty:payAccount:queryAccounts')")
  public List<String> queryAccounts() {
    return payAccountService.queryAccounts();
  }
}

package com.gameplat.admin.controller.open.thirdParty;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.PayAccount;
import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.admin.service.PayAccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/payAccount")
public class PayAccountController {

  @Autowired private PayAccountService payAccountService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:remove')")
  public void remove(@PathVariable Long id) {
    payAccountService.delete(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:add')")
  public void add(@RequestBody PayAccountAddDTO dto) {
    payAccountService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:edit')")
  public void edit(@RequestBody PayAccountEditDTO dto) {
    payAccountService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:editStatus')")
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

  @GetMapping("/queryOwners")
  @PreAuthorize("hasAuthority('thirdParty:payAccount:queryOwners')")
  public List<String> queryOwners() {
    return payAccountService.queryOwners();
  }
}

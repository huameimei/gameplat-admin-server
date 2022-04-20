package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.vo.PpMerchantVO;
import com.gameplat.admin.service.PpMerchantService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.PpMerchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/thirdParty/ppMerchants")
public class PpMerchantController {

  @Autowired private PpMerchantService ppMerchantService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'删除出款商户id=' + #id")
  public void remove(@PathVariable Long id) {
    ppMerchantService.delete(id);
  }

  @GetMapping("/queryMerchant")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:queryMerchant')")
  public PpMerchantVO getPpMerchantById(Long id) {
    return ppMerchantService.getPpMerchantById(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'新增出款商户name=' + #dto.name")
  public void add(@RequestBody PpMerchantAddDTO dto) {
    ppMerchantService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改出款商户id=' + #dto.id")
  public void edit(@RequestBody PpMerchantEditDTO dto) {
    ppMerchantService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改出款商户状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    ppMerchantService.updateStatus(id, status);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:view')")
  public IPage<PpMerchantVO> getPage(Page<PpMerchant> page, Integer status, String name) {
    return ppMerchantService.queryPage(page, status, name);
  }

  @GetMapping("/queryAllMerchant")
//  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:queryAllMerchant')")
  public List<PpMerchant> getAllMerchant() {
    return ppMerchantService.queryAllMerchant(SwitchStatusEnum.ENABLED.getValue());
  }
}

package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.TpMerchant;
import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.admin.service.TpMerchantService;
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
@RequestMapping("/api/admin/thirdParty/tpMerchants")
public class TpMerchantController {

  @Autowired private TpMerchantService tpMerchantService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:remove')")
  public void remove(@PathVariable Long id) {
    tpMerchantService.delete(id);
  }

  @GetMapping("/queryMerchant")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:queryMerchant')")
  public TpMerchantPayTypeVO getTpMerchantById(Long id) {
    return tpMerchantService.getTpMerchantById(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:add')")
  public void add(@RequestBody TpMerchantAddDTO dto) {
    tpMerchantService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:edit')")
  public void edit(@RequestBody TpMerchantEditDTO dto) {
    tpMerchantService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:editStatus')")
  public void updateStatus(Long id, Integer status) {
    tpMerchantService.updateStatus(id, status);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:page')")
  public IPage<TpMerchantVO> getPage(Page<TpMerchant> page, Integer status, String name) {
    return tpMerchantService.queryPage(page, status, name);
  }

  @GetMapping("/queryAllMerchant")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:queryAllMerchant')")
  public List<TpMerchantVO> getAllMerchant() {
    Integer status = 1; // 获取可用商户
    return tpMerchantService.queryAllMerchant(status);
  }
}

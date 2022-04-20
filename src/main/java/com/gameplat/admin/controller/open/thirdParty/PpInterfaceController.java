package com.gameplat.admin.controller.open.thirdParty;

import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.model.entity.pay.PpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/thirdParty/ppInterfaces")
public class PpInterfaceController {

  @Autowired private PpInterfaceService ppInterfaceService;

  @GetMapping("/queryAll")
//  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryAll')")
  public List<PpInterfaceVO> getAll() {
    return ppInterfaceService.queryAll();
  }

  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:view')")
  public List<PpInterface> getList() {
    return ppInterfaceService.list();
  }

  @GetMapping("/queryPpInterface")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryPpInterface')")
  public PpInterface queryPpInterface(Long id) {
    return ppInterfaceService.getById(id);
  }

  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:synchronization')")
  public void synchronization(@RequestBody PpInterface ppInterface) {
    ppInterfaceService.synchronization(ppInterface);
  }
}

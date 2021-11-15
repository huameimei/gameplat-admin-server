package com.gameplat.admin.controller.open.thirdParty;

import com.gameplat.admin.model.domain.PpInterface;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/ppInterfaces")
public class PpInterfaceController {

  @Autowired
  private PpInterfaceService ppInterfaceService;

  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryAll')")
  public List<PpInterfaceVO> getAll() {
    return ppInterfaceService.queryAll();
  }

  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryList')")
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

package com.gameplat.admin.controller.open.thirdParty;

import com.gameplat.admin.model.domain.TpInterface;
import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/thirdParty/tpInterfaces")
public class TpInterfaceController {

  @Autowired private TpInterfaceService tpInterfaceService;

  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryAll')")
  public List<TpInterfaceVO> getAll() {
    return tpInterfaceService.queryAll();
  }

  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryList')")
  public List<TpInterface> getList() {
    return tpInterfaceService.list();
  }

  @GetMapping("/queryTpInterfacePayType")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryTpInterfacePayType')")
  public TpInterfacePayTypeVo queryTpInterfacePayType(Long id) {
    return tpInterfaceService.queryTpInterfacePayType(id);
  }

  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:synchronization')")
  public void synchronization(@RequestBody TpInterface tpInterface) {
    tpInterfaceService.synchronization(tpInterface);
  }
}

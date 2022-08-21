package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.PpInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "代付管理")
@RestController
@RequestMapping("/api/admin/thirdParty/ppInterfaces")
public class PpInterfaceController {

  @Autowired private PpInterfaceService ppInterfaceService;

  @Operation(summary = "查询全部")
  @GetMapping("/queryAll")
  //  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryAll')")
  public List<PpInterfaceVO> getAll() {
    return ppInterfaceService.queryAll();
  }

  @Operation(summary = "查询全部")
  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:view')")
  public List<PpInterface> getList() {
    return ppInterfaceService.list();
  }

  @Operation(summary = "根据ID查询")
  @GetMapping("/queryPpInterface")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryPpInterface')")
  public PpInterface queryPpInterface(Long id) {
    return ppInterfaceService.getById(id);
  }

  @Operation(summary = "同步")
  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:synchronization')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代付管理-->同步:' + #ppInterface")
  public void synchronization(@RequestBody PpInterface ppInterface) {
    ppInterfaceService.synchronization(ppInterface);
  }
}

package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.admin.service.PpInterfaceService;
import com.gameplat.model.entity.pay.PpInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "代付管理")
@RestController
@RequestMapping("/api/admin/thirdParty/ppInterfaces")
public class PpInterfaceController {

  @Autowired private PpInterfaceService ppInterfaceService;

  @ApiOperation("查询全部")
  @GetMapping("/queryAll")
  //  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryAll')")
  public List<PpInterfaceVO> getAll() {
    return ppInterfaceService.queryAll();
  }

  @ApiOperation("查询全部")
  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:view')")
  public List<PpInterface> getList() {
    return ppInterfaceService.list();
  }

  @ApiOperation("根据ID查询")
  @GetMapping("/queryPpInterface")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:queryPpInterface')")
  public PpInterface queryPpInterface(Long id) {
    return ppInterfaceService.getById(id);
  }

  @ApiOperation("同步")
  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:ppInterfaces:synchronization')")
  public void synchronization(@RequestBody PpInterface ppInterface) {
    ppInterfaceService.synchronization(ppInterface);
  }
}

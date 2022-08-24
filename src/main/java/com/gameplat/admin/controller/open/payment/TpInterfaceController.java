package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.TpInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "第三方支付接口")
@RestController
@RequestMapping("/api/admin/thirdParty/tpInterfaces")
public class TpInterfaceController {

  @Autowired private TpInterfaceService tpInterfaceService;

  @Operation(summary = "查询")
  @GetMapping("/queryAll")
  //  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryAll')")
  public List<TpInterfaceVO> getAll() {
    return tpInterfaceService.queryAll();
  }

  @Operation(summary = "获取全部")
  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:view')")
  public List<TpInterface> getList() {
    return tpInterfaceService.list();
  }

  @Operation(summary = "根据ID查询")
  @GetMapping("/queryTpInterfacePayType")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryTpInterfacePayType')")
  public TpInterfacePayTypeVo queryTpInterfacePayType(Long id) {
    return tpInterfaceService.queryTpInterfacePayType(id);
  }

  @Operation(summary = "同步")
  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:synchronization')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'第三方支付接口-->同步:' + #tpInterface")
  public void synchronization(@RequestBody TpInterface tpInterface) {
    tpInterfaceService.synchronization(tpInterface);
  }
}

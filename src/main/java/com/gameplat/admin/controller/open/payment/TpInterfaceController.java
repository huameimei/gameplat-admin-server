package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import com.gameplat.model.entity.pay.TpInterface;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "第三方支付接口")
@RestController
@RequestMapping("/api/admin/thirdParty/tpInterfaces")
public class TpInterfaceController {

  @Autowired private TpInterfaceService tpInterfaceService;

  @ApiOperation("查询")
  @GetMapping("/queryAll")
  //  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryAll')")
  public List<TpInterfaceVO> getAll() {
    return tpInterfaceService.queryAll();
  }

  @ApiOperation("获取全部")
  @GetMapping("/queryList")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:view')")
  public List<TpInterface> getList() {
    return tpInterfaceService.list();
  }

  @ApiOperation("根据ID查询")
  @GetMapping("/queryTpInterfacePayType")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:queryTpInterfacePayType')")
  public TpInterfacePayTypeVo queryTpInterfacePayType(Long id) {
    return tpInterfaceService.queryTpInterfacePayType(id);
  }

  @ApiOperation("同步")
  @PostMapping("/synchronization")
  @PreAuthorize("hasAuthority('thirdParty:tpInterfaces:synchronization')")
  public void synchronization(@RequestBody TpInterface tpInterface) {
    tpInterfaceService.synchronization(tpInterface);
  }
}

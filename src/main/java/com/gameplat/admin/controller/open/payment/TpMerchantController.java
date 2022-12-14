package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.admin.service.TpMerchantService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.TpMerchant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "第三方支付商户")
@RestController
@RequestMapping("/api/admin/thirdParty/tpMerchants")
public class TpMerchantController {

  @Autowired private TpMerchantService tpMerchantService;

  @Operation(summary = "删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除入款商户id=' + #id")
  public void remove(@PathVariable Long id) {
    tpMerchantService.delete(id);
  }

  @Operation(summary = "根据ID查询")
  @GetMapping("/queryMerchant")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:queryMerchant')")
  public TpMerchantPayTypeVO getTpMerchantById(Long id) {
    return tpMerchantService.getTpMerchantById(id);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增入款商户name=' + #dto.name")
  public void add(@RequestBody TpMerchantAddDTO dto) {
    tpMerchantService.save(dto);
  }

  @Operation(summary = "修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改入款商户id=' + #dto.id")
  public void edit(@RequestBody TpMerchantEditDTO dto) {
    tpMerchantService.update(dto);
  }

  @Operation(summary = "编辑状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改入款商户状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    tpMerchantService.updateStatus(id, status);
  }

  @Operation(summary = "查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:tpMerchants:view')")
  public IPage<TpMerchantVO> getPage(Page<TpMerchant> page, Integer status, String name) {
    return tpMerchantService.queryPage(page, status, name);
  }

  @Operation(summary = "查询全部")
  @GetMapping("/queryAllMerchant")
  public List<TpMerchantVO> getAllMerchant() {
    return tpMerchantService.queryAllMerchant(SwitchStatusEnum.ENABLED.getValue());
  }
}

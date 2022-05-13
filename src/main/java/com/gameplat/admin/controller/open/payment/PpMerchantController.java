package com.gameplat.admin.controller.open.payment;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "代付商户")
@RestController
@RequestMapping("/api/admin/thirdParty/ppMerchants")
public class PpMerchantController {

  @Autowired private PpMerchantService ppMerchantService;

  @ApiOperation("删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'删除出款商户id=' + #id")
  public void remove(@PathVariable Long id) {
    ppMerchantService.delete(id);
  }

  @ApiOperation("根据ID查询")
  @GetMapping("/queryMerchant")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:queryMerchant')")
  public PpMerchantVO getPpMerchantById(Long id) {
    return ppMerchantService.getPpMerchantById(id);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'新增出款商户name=' + #dto.name")
  public void add(@RequestBody PpMerchantAddDTO dto) {
    ppMerchantService.save(dto);
  }

  @ApiOperation("修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改出款商户id=' + #dto.id")
  public void edit(@RequestBody PpMerchantEditDTO dto) {
    ppMerchantService.update(dto);
  }

  @ApiOperation("编辑状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改出款商户状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    ppMerchantService.updateStatus(id, status);
  }

  @ApiOperation("查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:view')")
  public IPage<PpMerchantVO> getPage(Page<PpMerchant> page, Integer status, String name) {
    return ppMerchantService.queryPage(page, status, name);
  }

  @ApiOperation("获取全部商户")
  @GetMapping("/queryAllMerchant")
  //  @PreAuthorize("hasAuthority('thirdParty:ppMerchants:queryAllMerchant')")
  public List<PpMerchant> getAllMerchant() {
    return ppMerchantService.queryAllMerchant(SwitchStatusEnum.ENABLED.getValue());
  }
}

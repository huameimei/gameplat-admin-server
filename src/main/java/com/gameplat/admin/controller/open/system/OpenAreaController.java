package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.dto.SmsAreaQueryDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysSmsArea;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** 区号设置控制器 */
@Tag(name = "区号设置API")
@RestController
@RequestMapping("/api/admin/system/smsArea")
public class OpenAreaController {

  @Autowired private SysSmsAreaService areaService;

  @Operation(summary = "列表查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:area:view')")
  public IPage<SysSmsAreaVO> findSmsAreaList(
      @Parameter(hidden = true) PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO) {
    return areaService.findSmsAreaList(page, queryDTO);
  }

  @Operation(summary = "新增")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:area:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'区号设置API-->新增:' + #addDTO")
  public void saveArea(@Validated @RequestBody SmsAreaAddDTO addDTO) {
    areaService.addSmsArea(addDTO);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:area:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'区号设置API-->新增:' + #addDTO")
  public void updateArea(@Validated @RequestBody SmsAreaEditDTO editDTO) {
    areaService.editSmsArea(editDTO);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('system:area:remove')")
  public void deleteById(@PathVariable Long id) {
    areaService.deleteAreaById(id);
  }

  @Operation(summary = "改变状态")
  @PostMapping("/changeStatus/{id}/{status}")
  @PreAuthorize("hasAuthority('system:area:edit')")
  public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    areaService.changeStatus(id, status);
  }

  @Operation(summary = "设置默认区号")
  @PostMapping("/setDefaultStatus/{id}/{status}")
  @PreAuthorize("hasAuthority('system:area:edit')")
  public void setDefaultStatus(@PathVariable Long id, @PathVariable Integer status) {
    areaService.setDefaultStatus(id, status);
  }
}

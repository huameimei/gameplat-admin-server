package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.admin.service.VersionControlService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.VersionControl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "APP版本管理")
@RestController
@RequestMapping("/api/admin/operator/package")
public class OperAppVersionController {

  @Autowired private VersionControlService sysService;

  @Operation(summary = "获取发版信息列表")
  @GetMapping("/getVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:view')")
  public IPage<VersionControl> packageInfo(PageDTO<VersionControl> page, VersionControlDTO dto) {
    return sysService.getSysPackageInfo(page, dto);
  }

  @Operation(summary = "新增发版")
  @PostMapping("/createVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'APP版本管理-->新增发版:' + #dto")
  public boolean createPackageInfo(@RequestBody VersionControlDTO dto) {
    return sysService.createSysPackageInfo(dto);
  }

  @Operation(summary = "修改发版信息")
  @PostMapping("/editVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'APP版本管理-->修改发版信息:' + #dto")
  public int editVersionInfo(@RequestBody VersionControlDTO dto) {
    return sysService.editSysPackageInfo(dto);
  }

  @Operation(summary = "删除发版信息")
  @PostMapping("/removeVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'APP版本管理-->删除发版信息:' + #id")
  public boolean removeVersionInfo(Integer id) {
    return sysService.removeSysPackageInfo(id);
  }
}

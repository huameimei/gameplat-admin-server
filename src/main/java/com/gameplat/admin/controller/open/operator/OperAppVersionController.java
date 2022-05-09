package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.admin.service.VersionControlService;
import com.gameplat.model.entity.VersionControl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "APP版本管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/package")
public class OperAppVersionController {

  @Autowired private VersionControlService sysService;

  @ApiOperation("获取发版信息列表")
  @GetMapping("/getVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:view')")
  public IPage<VersionControl> packageInfo(PageDTO<VersionControl> page, VersionControlDTO dto) {
    return sysService.getSysPackageInfo(page, dto);
  }

  @ApiOperation("新增发版")
  @PostMapping("/createVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:add')")
  public boolean createPackageInfo(@RequestBody VersionControlDTO dto) {
    return sysService.createSysPackageInfo(dto);
  }

  @ApiOperation("修改发版信息")
  @PostMapping("/editVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:edit')")
  public int editVersionInfo(@RequestBody VersionControlDTO dto) {
    return sysService.editSysPackageInfo(dto);
  }

  @ApiOperation("删除发版信息")
  @DeleteMapping("/removeVersionInfo")
  @PreAuthorize("hasAuthority('operator:package:remove')")
  public boolean removeVersionInfo(Integer id) {
    return sysService.removeSysPackageInfo(id);
  }
}

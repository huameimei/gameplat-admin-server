package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.VersionDTO;
import com.gameplat.admin.service.SysVersionService;
import com.gameplat.model.entity.sys.SysVersion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "版本记录")
@RestController
@RequestMapping("/api/admin/system/version")
public class OperVersionController {

  @Autowired private SysVersionService versionService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:version:view')")
  public IPage<SysVersion> list(PageDTO<SysVersion> page, VersionDTO dto) {
    return versionService.selectVersionList(page, dto);
  }
}

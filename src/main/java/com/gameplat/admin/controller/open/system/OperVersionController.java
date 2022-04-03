package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.VersionDTO;
import com.gameplat.admin.service.SysVersionService;
import com.gameplat.model.entity.sys.SysVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本记录控制器
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/system/version")
public class OperVersionController {

  @Autowired private SysVersionService versionService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:version:view')")
  public IPage<SysVersion> list(PageDTO<SysVersion> page, VersionDTO dto) {
    return versionService.selectVersionList(page, dto);
  }
}

package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.VersionControl;
import com.gameplat.admin.model.domain.SysVersion;
import com.gameplat.admin.model.dto.VersionControlDTO;
import com.gameplat.admin.model.dto.VersionDTO;
import com.gameplat.admin.service.VersionControlService;
import com.gameplat.admin.service.SysVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 版本记录控制器
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/system/version")
public class OperVersionController {

  private final SysVersionService versionService;

  @GetMapping("/list")
  public IPage<SysVersion> list(PageDTO<SysVersion> page, VersionDTO versionDTO) {
    return versionService.selectVersionList(page, versionDTO);
  }





}

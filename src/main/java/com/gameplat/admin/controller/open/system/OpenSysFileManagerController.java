package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysFileManagerQueryDTO;
import com.gameplat.admin.model.vo.SysFileManagerVO;
import com.gameplat.admin.service.SysFileManagerService;
import com.gameplat.model.entity.sys.SysFileManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传管理
 *
 * @author aBen
 */
@RestController
@Tag(name = "文件上传管理")
@RequestMapping("/api/admin/system/file/manager")
public class OpenSysFileManagerController {

  @Autowired private SysFileManagerService sysFileManagerService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:file:view')")
  public IPage<SysFileManagerVO> list(
      PageDTO<SysFileManager> page, SysFileManagerQueryDTO queryDTO) {
    return sysFileManagerService.list(page, queryDTO);
  }

  @Operation(summary = "上传图片")
  @PostMapping("/upload")
  @PreAuthorize("hasAuthority('system:file:upload')")
  public String upload(@RequestPart MultipartFile file) {
    return sysFileManagerService.upload(file);
  }

  @Operation(summary = "删除图片")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('system:file:delete')")
  public void delete(String ids) {
    sysFileManagerService.delete(ids);
  }
}

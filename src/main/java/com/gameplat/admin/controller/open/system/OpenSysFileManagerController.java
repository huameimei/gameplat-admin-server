package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysFileManagerQueryDTO;
import com.gameplat.admin.model.vo.SysFileManagerVO;
import com.gameplat.admin.service.SysFileManagerService;
import com.gameplat.model.entity.sys.SysFileManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aBen
 * @date 2022/4/9 23:20
 * @desc
 */
@Slf4j
@RestController
@Api(tags = "文件上传管理")
@RequestMapping("/api/admin/system/file/manager")
public class OpenSysFileManagerController {

  @Autowired
  private SysFileManagerService sysFileManagerService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:file:view')")
  public IPage<SysFileManagerVO> list(PageDTO<SysFileManager> page, SysFileManagerQueryDTO queryDTO) {
    return sysFileManagerService.list(page, queryDTO);
  }

  @ApiOperation(value = "上传图片")
  @PostMapping("/upload")
  @PreAuthorize("hasAuthority('system:file:upload')")
  public String upload(@RequestPart MultipartFile file) {
    return sysFileManagerService.upload(file);
  }

  @ApiOperation(value = "上传图片")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:file:delete')")
  public void delete(String ids) {
    sysFileManagerService.delete(ids);
  }

}

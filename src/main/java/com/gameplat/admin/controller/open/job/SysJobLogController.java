package com.gameplat.admin.controller.open.job;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.SysJobLog;
import com.gameplat.admin.service.SysJobLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/admin/sys/job/log")
public class SysJobLogController {
  @Autowired private SysJobLogService sysJobLogService;

  @GetMapping("/list")
  public IPage<SysJobLog> list(PageDTO<SysJobLog> page, SysJobLog dto) {
    return sysJobLogService.queryPage(page, dto);
  }

  @PostMapping("/remove")
  public void remove(String ids) {
    if (StrUtil.isNotBlank(ids)) {
      sysJobLogService.deleteJobLogByIds(ids);
    }
  }

  @PostMapping("/clean")
  public void clean() {
    sysJobLogService.cleanJobLog();
  }
}

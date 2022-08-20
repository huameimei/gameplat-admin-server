package com.gameplat.admin.controller.open.job;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.feign.TaskServiceFeignClient;
import com.gameplat.model.entity.sys.SysJobLog;
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
  @Autowired private TaskServiceFeignClient taskServiceFeignClient;

  @GetMapping("/list")
  public Page<SysJobLog> list(PageDTO<SysJobLog> page, SysJobLog dto) {
    BeanUtil.copyProperties(page, dto);
    return taskServiceFeignClient.list(dto);
  }

  @PostMapping("/remove")
  public void remove(String ids) {
    taskServiceFeignClient.remove(ids);
  }

  @PostMapping("/clean")
  public void clean() {
    taskServiceFeignClient.clean();
  }
}

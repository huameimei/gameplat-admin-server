package com.gameplat.admin.controller.open.job;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.feign.TaskServiceFeignClient;
import com.gameplat.model.entity.sys.SysJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @Description : 工资期数 @Author : cc @Date : 2022/4/2 */
@RestController
@RequestMapping("/api/admin/sys/job")
public class SysJobController {

  @Autowired private TaskServiceFeignClient taskServiceFeignClient;

  @GetMapping("/list")
  public Page<SysJob> list(PageDTO<SysJob> page, SysJob dto) {
    BeanUtil.copyProperties(page, dto);
    return taskServiceFeignClient.list(dto);
  }

  @PostMapping("/add")
  public void add(@RequestBody SysJob dto) {
    taskServiceFeignClient.add(dto);
  }

  @PostMapping("/edit")
  public void edit(@RequestBody SysJob dto) {
    taskServiceFeignClient.edit(dto);
  }

  @PostMapping("/remove")
  public void remove(@RequestBody List<String> idArray) {
    taskServiceFeignClient.remove(idArray);
  }

  @PostMapping("/changeStatus")
  public void changeStatus(@RequestBody SysJob job) {
    taskServiceFeignClient.changeStatus(job);
  }

  @PostMapping("/run")
  public void run(@RequestBody SysJob job) {
    taskServiceFeignClient.run(job);
  }

  @PostMapping("/checkCronExpressionIsValid")
  public boolean checkCronExpressionIsValid(@RequestBody SysJob job) {
    return taskServiceFeignClient.checkCronExpressionIsValid(job);
  }
}

package com.gameplat.admin.controller.open.job;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.SysJob;
import com.gameplat.admin.service.SysJobService;
import com.gameplat.admin.util.TaskException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @Description : 工资期数 @Author : cc @Date : 2022/4/2 */
@RestController
@RequestMapping("/api/admin/sys/job")
public class SysJobController {

  @Autowired private SysJobService sysJobService;

  @GetMapping("/list")
  public IPage<SysJob> list(PageDTO<SysJob> page, SysJob dto) {
    return sysJobService.queryPage(page, dto);
  }

  @PostMapping("/add")
  public void add(@RequestBody SysJob dto) throws SchedulerException, TaskException {
    sysJobService.add(dto);
  }

  @PostMapping("/edit")
  public void edit(@RequestBody SysJob dto) throws SchedulerException, TaskException {
    sysJobService.edit(dto);
  }

  @PostMapping("/remove")
  public void remove(@RequestBody List<String> idArray) throws SchedulerException {
    sysJobService.deleteByIds(idArray);
  }

  @PostMapping("/changeStatus")
  public void changeStatus(@RequestBody SysJob job) throws SchedulerException {
    SysJob newJob = sysJobService.getById(job.getJobId());
    newJob.setStatus(job.getStatus());
    sysJobService.changeStatus(newJob);
  }

  @PostMapping("/run")
  public void run(@RequestBody SysJob job) throws SchedulerException {
    sysJobService.run(job);
  }

  @PostMapping("/checkCronExpressionIsValid")
  public boolean checkCronExpressionIsValid(@RequestBody SysJob job) {
    return sysJobService.checkCronExpressionIsValid(job.getCronExpression());
  }
}

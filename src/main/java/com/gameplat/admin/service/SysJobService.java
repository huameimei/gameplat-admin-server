package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.SysJob;
import com.gameplat.admin.util.TaskException;
import org.quartz.SchedulerException;

import java.util.List;

/** @Description : 工资期数 @Author : cc @Date : 2022/4/2 */
public interface SysJobService extends IService<SysJob> {
  IPage<SysJob> queryPage(PageDTO<SysJob> page, SysJob dto);

  void add(SysJob dto) throws SchedulerException, TaskException;

  void edit(SysJob dto) throws SchedulerException, TaskException;

  void deleteByIds(List<String> ids) throws SchedulerException;

  void deleteJob(SysJob job) throws SchedulerException;

  int changeStatus(SysJob job) throws SchedulerException;

  int resumeJob(SysJob job) throws SchedulerException;

  int pauseJob(SysJob job) throws SchedulerException;

  void run(SysJob job) throws SchedulerException;

  boolean checkCronExpressionIsValid(String cronExpression);
}

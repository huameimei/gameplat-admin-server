package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.ScheduleConstants;
import com.gameplat.admin.mapper.SysJobMapper;
import com.gameplat.admin.model.bean.SysJob;
import com.gameplat.admin.service.SysJobService;
import com.gameplat.admin.util.CronUtils;
import com.gameplat.admin.util.ScheduleUtils;
import com.gameplat.admin.util.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {

  @Autowired private Scheduler scheduler;

  @Autowired private SysJobMapper sysJobMapper;

  @Override
  public IPage<SysJob> queryPage(PageDTO<SysJob> page, SysJob dto) {
    LambdaQueryWrapper<SysJob> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(StrUtil.isNotBlank(dto.getJobName()), SysJob::getJobName, dto.getJobName());
    queryWrapper.like(
        StrUtil.isNotBlank(dto.getMethodName()), SysJob::getMethodName, dto.getMethodName());
    queryWrapper.eq(StrUtil.isNotBlank(dto.getStatus()), SysJob::getStatus, dto.getStatus());
    queryWrapper.orderByDesc(SysJob::getCreateTime);
    return sysJobMapper.selectPage(page, queryWrapper);
  }

  @Override
  @Transactional
  public void add(SysJob dto) throws SchedulerException, TaskException {
    dto.setStatus(ScheduleConstants.Status.PAUSE.getValue());
    boolean save = this.save(dto);
    if (save) {
      ScheduleUtils.createScheduleJob(scheduler, dto);
    }
  }

  @Override
  @Transactional
  public void edit(SysJob dto) throws SchedulerException, TaskException {
    boolean update = this.updateById(dto);
    if (update) {
      ScheduleUtils.updateScheduleJob(scheduler, dto);
    }
  }

  @Override
  @Transactional
  public void deleteByIds(List<String> ids) throws SchedulerException {
    for (String jobId : ids) {
      SysJob job = sysJobMapper.selectById(Long.valueOf(jobId));
      deleteJob(job);
    }
  }

  @Override
  @Transactional
  public void deleteJob(SysJob job) throws SchedulerException {
    int rows = sysJobMapper.deleteById(job.getJobId());
    if (rows > 0) {
      ScheduleUtils.deleteScheduleJob(scheduler, job.getJobId());
    }
  }

  @Override
  @Transactional
  public int changeStatus(SysJob job) throws SchedulerException {
    int rows = 0;
    String status = job.getStatus();
    if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
      rows = resumeJob(job);
    } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
      rows = pauseJob(job);
    }

    return rows;
  }

  @Transactional
  @Override
  public int resumeJob(SysJob job) throws SchedulerException {
    job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
    int rows = sysJobMapper.updateById(job);
    if (rows > 0) {
      ScheduleUtils.resumeJob(scheduler, job.getJobId());
    }
    return rows;
  }

  @Override
  @Transactional
  public int pauseJob(SysJob job) throws SchedulerException {
    job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
    int rows = sysJobMapper.updateById(job);
    if (rows > 0) {
      ScheduleUtils.pauseJob(scheduler, job.getJobId());
    }
    return rows;
  }

  @Override
  @Transactional
  public void run(SysJob job) throws SchedulerException {
    ScheduleUtils.run(scheduler, this.getById(job.getJobId()));
  }

  @Override
  public boolean checkCronExpressionIsValid(String cronExpression) {
    return CronUtils.isValid(cronExpression);
  }
}

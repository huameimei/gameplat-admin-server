package com.gameplat.admin.util;

import com.gameplat.admin.model.bean.SysJob;
import org.quartz.JobExecutionContext;

/** 定时任务处理（允许并发执行） */
public class QuartzJobExecution extends AbstractQuartzJob {
  @Override
  protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
    JobInvokeUtil.invokeMethod(sysJob);
  }
}

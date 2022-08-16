package com.gameplat.admin.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/** 定时任务调度日志表 sys_job_log */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_job_log")
public class SysJobLog implements Serializable {

  private static final long serialVersionUID = 1L;

  /** ID */
  @TableId(type = IdType.AUTO)
  private Long jobLogId;

  /** 任务名称 */
  private String jobName;

  /** 任务组名 */
  private String jobGroup;

  /** 任务方法 */
  private String methodName;

  /** 方法参数 */
  private String methodParams;

  /** 日志信息 */
  private String jobMessage;

  /** 执行状态（0正常 1失败） */
  private String status;

  /** 异常信息 */
  private String exceptionInfo;

  /** 开始时间 */
  private Date startTime;

  /** 结束时间 */
  private Date endTime;
}

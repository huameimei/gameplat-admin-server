package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.SysJobLog;

/** @Description : 工资期数 @Author : cc @Date : 2022/4/2 */
public interface SysJobLogService extends IService<SysJobLog> {
  IPage<SysJobLog> queryPage(PageDTO<SysJobLog> page, SysJobLog dto);

  void addJobLog(SysJobLog jobLog);

  void deleteJobLogByIds(String ids);

  void cleanJobLog();
}

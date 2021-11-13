package com.gameplat.admin.handler;

import com.gameplat.admin.feign.RemoteLogService;
import com.gameplat.common.log.SysLog;
import com.gameplat.log.handler.AbstractLogProcessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志处理
 *
 * @author robben
 */
@Component
public class LogProcessHandler extends AbstractLogProcessHandler {

  @Autowired private RemoteLogService remoteLogService;

  @Override
  public void saveErrorLog(SysLog log) {
    remoteLogService.saveErrorLog(log);
  }

  @Override
  public void saveLoginLog(SysLog log) {
    remoteLogService.saveLoginLog(log);
  }

  @Override
  public void saveOperLog(SysLog log) {
    remoteLogService.saveOperLog(log);
  }
}

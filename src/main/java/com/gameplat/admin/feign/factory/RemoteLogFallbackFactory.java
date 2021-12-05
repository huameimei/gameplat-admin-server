package com.gameplat.admin.feign.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.feign.RemoteLogService;
import com.gameplat.admin.model.domain.SysLogLogin;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.vo.UserLogVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志 Feign接口 失败回调
 *
 * @author three
 */
@Slf4j
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

  @Override
  public RemoteLogService create(Throwable throwable) {
    log.error(throwable.getMessage());
    return new RemoteLogService() {

      @Override
      public void saveLoginLog(SysLog log) {}

      @Override
      public Result<Page<UserLogVO>> operList(LogDTO logDTO) {
        return Result.failed("获取数据失败");
      }

      @Override
      public Result<Page<SysLogLogin>> loginList(LogDTO logDTO) {
        return Result.failed("获取数据失败");
      }

      @Override
      public void saveOperLog(SysLog log) {}

      @Override
      public void saveErrorLog(SysLog log) {}
    };
  }
}

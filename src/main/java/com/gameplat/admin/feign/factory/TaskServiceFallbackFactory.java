package com.gameplat.admin.feign.factory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.feign.TaskServiceFeignClient;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.sys.SysJob;
import com.gameplat.model.entity.sys.SysJobLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志 Feign接口 失败回调
 *
 * @author three
 */
@Slf4j
@Component
public class TaskServiceFallbackFactory implements FallbackFactory<TaskServiceFeignClient> {

  @Override
  public TaskServiceFeignClient create(Throwable throwable) {
    log.error(throwable.getMessage());
    return new TaskServiceFeignClient() {
      @Override
      public Page<SysJob> list(SysJob dto) {
        return null;
      }

      @Override
      public void add(SysJob dto) {
        throw new ServiceException("添加定时任务失败");
      }

      @Override
      public void edit(SysJob dto) {
        throw new ServiceException("编辑定时任务失败");
      }

      @Override
      public void remove(List<String> idArray) {
        throw new ServiceException("删除定时任务失败");
      }

      @Override
      public void changeStatus(SysJob job) {
        throw new ServiceException("更新状态失败");
      }

      @Override
      public void run(SysJob job) {
        throw new ServiceException("立即执行失败");
      }

      @Override
      public boolean checkCronExpressionIsValid(SysJob job) {
        throw new ServiceException("校验失败");
      }

      @Override
      public Page<SysJobLog> list(SysJobLog dto) {
        return null;
      }

      @Override
      public void remove(String ids) {
        throw new ServiceException("删除定时任务日志失败");
      }

      @Override
      public void clean() {
        throw new ServiceException("清除日志失败");
      }
    };
  }
}

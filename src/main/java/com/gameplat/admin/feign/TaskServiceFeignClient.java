package com.gameplat.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.feign.factory.TaskServiceFallbackFactory;
import com.gameplat.common.game.config.FeignRestConfig;
import com.gameplat.model.entity.sys.SysJob;
import com.gameplat.model.entity.sys.SysJobLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/** @Description : 定时任务 @Author : cc @Date : 2022/8/20 */
@FeignClient(
    value = "task-service",
    configuration = FeignRestConfig.class,
    path = "/api/task/internal",
    fallbackFactory = TaskServiceFallbackFactory.class)
public interface TaskServiceFeignClient {

  @PostMapping("/job/list")
  Page<SysJob> list(@RequestBody SysJob dto);

  @PostMapping("/job/add")
  void add(@RequestBody SysJob dto);

  @PostMapping("/job/edit")
  void edit(@RequestBody SysJob dto);

  @PostMapping("/job/remove")
  void remove(@RequestBody List<String> idArray);

  @PostMapping("/job/changeStatus")
  void changeStatus(@RequestBody SysJob job);

  @PostMapping("/job/run")
  void run(@RequestBody SysJob job);

  @PostMapping("/job/checkCronExpressionIsValid")
  boolean checkCronExpressionIsValid(@RequestBody SysJob job);

  @PostMapping("/job/log/list")
  Page<SysJobLog> list(@RequestBody SysJobLog dto);

  @PostMapping("/job/log/remove")
  void remove(@RequestBody String ids);

  @PostMapping("/job/log/clean")
  void clean();
}

package com.gameplat.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.feign.factory.RemoteLogFallbackFactory;
import com.gameplat.admin.model.domain.SysLogLogin;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.vo.UserLogVO;
import com.gameplat.base.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 日志 Feign接口
 *
 * @author three
 */
@FeignClient(
    name = ServiceName.LOG_SERVICE,
    path = "/api/logs/internal",
    fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

  /**
   * 获取操作日志列表
   *
   * @param logDTO LogDTO
   * @return Page<UserLogVO>
   */
  @ResponseBody
  @RequestMapping(
      value = "/oper/list",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Result<Page<UserLogVO>> operList(@RequestBody LogDTO logDTO);

  @ResponseBody
  @RequestMapping(
      value = "/login/list",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Result<Page<SysLogLogin>> loginList(@RequestBody LogDTO logDTO);

  /**
   * 保存操作日志
   * @param log
   */
  @PostMapping(value = "/api/logs/internal/oper/save")
  void saveOperLog(@RequestBody SysLog log);

  /**
   * 保存登录日志
   *
   * @param log
   */
  @PostMapping(value = "/api/logs/internal/login/save")
  void saveLoginLog(@RequestBody SysLog log);

  /**
   * 保存bug日志
   *
   * @param log
   */
  @PostMapping(value = "/api/logs/internal/error/save")
  void saveErrorLog(@RequestBody SysLog log);
}

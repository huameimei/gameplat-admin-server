package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.service.SysLogService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.entity.MemberLogLogin;
import com.gameplat.common.model.entity.MemberLogOper;
import com.gameplat.common.model.vo.MemberLogVO;
import com.gameplat.common.util.EasyExcelUtil;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.redis.idempoten.AutoIdempotent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 业主日志管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/operator/logs")
public class OperLogsController {

  @Autowired private SysLogService logService;

  @GetMapping("/login/list")
  @PreAuthorize("hasAuthority('operator:loginLogs:view')")
  public IPage<MemberLogLogin> loginList(LogDTO logDTO) {
    return logService.selectLoginList(logDTO);
  }

  @PostMapping("/login/export")
  @PreAuthorize("hasAuthority('operator:loginLogs:export')")
  @AutoIdempotent(spelKey = "LoginLogExport", expir = 5000L)
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "导出会员登录日志")
  public void loginExport(@RequestBody LogDTO logDTO, HttpServletResponse response) {
    List<MemberLogVO> logs = logService.loginExport(logDTO);
    EasyExcelUtil.downExcel("会员登录日志", logs, MemberLogVO.class, response);
  }

  @GetMapping("/oper/list")
  @PreAuthorize("hasAuthority('operator:operLogs:view')")
  public IPage<MemberLogOper> operList(LogDTO logDTO) {
    return logService.selectOperList(logDTO);
  }

  @PostMapping("/oper/export")
  @PreAuthorize("hasAuthority('operator:operLogs:export')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "导出会员操作日志")
  @AutoIdempotent(spelKey = "OperLogExport", expir = 5000L)
  public void operExport(@RequestBody LogDTO logDTO, HttpServletResponse response) {
    List<MemberLogVO> logs = logService.operExport(logDTO);
    EasyExcelUtil.downExcel("会员操作日志", logs, MemberLogVO.class, response);
  }
}

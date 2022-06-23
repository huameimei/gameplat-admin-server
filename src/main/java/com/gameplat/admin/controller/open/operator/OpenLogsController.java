package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.service.SysLogService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.dto.LogDTO;
import com.gameplat.common.model.entity.MemberLogLogin;
import com.gameplat.common.model.entity.MemberLogOper;
import com.gameplat.common.model.vo.MemberLogVO;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Tag(name = "日志管理")
@RestController
@RequestMapping("/api/admin/operator/logs")
public class OpenLogsController {

  @Autowired private SysLogService logService;

  @Operation(summary = "查询登录日志")
  @GetMapping("/login/list")
  @PreAuthorize("hasAuthority('operator:loginLogs:view')")
  public IPage<MemberLogLogin> loginList(LogDTO logDTO) {
    return logService.selectLoginList(logDTO);
  }

  @Operation(summary = "导出登录日志")
  @PostMapping("/login/export")
  @PreAuthorize("hasAuthority('operator:loginLogs:export')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'导出会员登录日志'")
  public void loginExport(@RequestBody LogDTO logDTO, HttpServletResponse response) {
    List<MemberLogVO> logs = logService.loginExport(logDTO);
    EasyExcelUtil.downExcel("会员登录日志", logs, MemberLogVO.class, response);
  }

  @Operation(summary = "查询操作日志")
  @GetMapping("/oper/list")
  @PreAuthorize("hasAuthority('operator:operLogs:view')")
  public IPage<MemberLogOper> operList(LogDTO logDTO) {
    return logService.selectOperList(logDTO);
  }

  @Operation(summary = "导出操作日志")
  @PostMapping("/oper/export")
  @PreAuthorize("hasAuthority('operator:operLogs:export')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'导出会员操作日志'")
  public void operExport(@RequestBody LogDTO logDTO, HttpServletResponse response) {
    List<MemberLogVO> logs = logService.operExport(logDTO);
    EasyExcelUtil.downExcel("会员操作日志", logs, MemberLogVO.class, response);
  }
}

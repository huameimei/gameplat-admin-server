package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.model.entity.sys.SysEmail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Deprecated
@Api(tags = "邮件记录")
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/logs/email")
public class OpenEmailController {

  @Autowired private SysEmailService emailService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('logs:email:view')")
  public IPage<EmailVO> list(PageDTO<SysEmail> page, EmailDTO emailDTO) {
    return emailService.selectEmailList(page, emailDTO);
  }

  @ApiOperation("清空")
  @PostMapping("/clean")
  @PreAuthorize("hasAuthority('logs:email:clean')")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "清空邮件记录表")
  public void clean() {
    emailService.cleanEmail();
  }
}

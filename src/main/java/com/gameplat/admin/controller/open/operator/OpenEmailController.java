package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.model.entity.sys.SysEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件记录
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/logs/email")
public class OpenEmailController {

  @Autowired private SysEmailService emailService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('logs:email:view')")
  public IPage<EmailVO> list(PageDTO<SysEmail> page, EmailDTO emailDTO) {
    return emailService.selectEmailList(page, emailDTO);
  }

  @DeleteMapping("/clean")
  @PreAuthorize("hasAuthority('logs:email:clean')")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "清空邮件记录表")
  public void clean() {
    emailService.cleanEmail();
  }
}

package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysEmail;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/admin/system/email")
public class OpenEmailController {

  @Autowired private SysEmailService emailService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:email:view')")
  public IPage<EmailVO> list(PageDTO<SysEmail> page, EmailDTO emailDTO) {
    return emailService.selectEmailList(page, emailDTO);
  }

  @DeleteMapping("/clean")
  @PreAuthorize("hasAuthority('system:email:clean')")
  @Log(module = ServiceName.ADMIN_SERVICE, param = true, desc = "清空邮件记录表")
  public void clean() {
    emailService.cleanEmail();
  }
}

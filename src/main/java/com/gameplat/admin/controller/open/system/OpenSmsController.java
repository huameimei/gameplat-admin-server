package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysSMS;
import com.gameplat.admin.model.dto.SmsDTO;
import com.gameplat.admin.model.vo.SMSVO;
import com.gameplat.admin.service.SysSmsService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.SmsCodeType;
import com.gameplat.log.annotation.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 短信记录
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/system/sms")
public class OpenSmsController {

  @Autowired private SysSmsService smsService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:sms:view')")
  public IPage<SMSVO> list(PageDTO<SysSMS> page, SmsDTO smsDTO) {
    return smsService.selectSmsList(page, smsDTO);
  }

  @DeleteMapping("/clean")
  @PreAuthorize("hasAuthority('system:sms:clean')")
  @Log(module = ServiceName.ADMIN_SERVICE, param = true, desc = "清空短信记录表")
  public void clean() {
    smsService.cleanSms();
  }

  @GetMapping("/smsTypeList")
  public List<Map<String, Object>> smsTypeList() {
    return SmsCodeType.getAllList();
  }
}

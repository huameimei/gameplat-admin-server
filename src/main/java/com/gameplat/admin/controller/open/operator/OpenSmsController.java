package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SmsDTO;
import com.gameplat.admin.model.vo.SMSVO;
import com.gameplat.admin.service.SysSmsService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.SmsCodeType;
import com.gameplat.log.annotation.Log;
import com.gameplat.model.entity.sys.SysSMS;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "短信记录")
@RestController
@RequestMapping("/api/admin/operator/logs/sms")
public class OpenSmsController {

  @Autowired private SysSmsService smsService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:sms:view')")
  public IPage<SMSVO> list(PageDTO<SysSMS> page, SmsDTO smsDTO) {
    return smsService.selectSmsList(page, smsDTO);
  }

  @Operation(summary = "清空")
  @PostMapping("/clean")
  @PreAuthorize("hasAuthority('system:sms:clean')")
  @Log(module = ServiceName.ADMIN_SERVICE, param = true, desc = "清空短信记录表")
  public void clean() {
    smsService.cleanSms();
  }

  @Operation(summary = "获取短信类型")
  @GetMapping("/smsTypeList")
  public List<Map<String, Object>> smsTypeList() {
    return SmsCodeType.getAllList();
  }
}

package com.gameplat.admin.controller.open.common;

import cn.hutool.core.date.DateTime;
import com.gameplat.admin.service.ExternalDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/** @Description : cc @Author : cc @Date : 2022/4/17 */
@Tag(name = "老平台迁移")
@RestController
@RequestMapping("/api/admin/external")
@Slf4j
public class OpenMoveController {

  @Autowired
  private ExternalDataService externalDataService;

  @PostMapping("/import")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'导入外部数据'")
  public void uploadInsert(@RequestPart(value = "file") MultipartFile file,
                                 HttpServletRequest request) {
    String username = SecurityUserHolder.getUsername();
    log.info("{}--{}开始导入外部数据", DateTime.now(), username);
    try {
      externalDataService.dealData(username, file, request);
    }catch (Exception e) {
      e.printStackTrace();
      log.info("导入外部数据错误：{}", e);
    }

  }

  @GetMapping("/enPswd")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'更新密码加密'")
  public void enPswd() {
    log.info("开始处理密码加密 默认将注册类型为 -1的会员的明文密码加密存储");
    externalDataService.enPswd();
  }
}

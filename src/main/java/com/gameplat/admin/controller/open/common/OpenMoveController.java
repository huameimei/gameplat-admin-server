package com.gameplat.admin.controller.open.common;

import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @Description : cc @Author : cc @Date : 2022/4/17 */
@Slf4j
@Validated
@Api(tags = "老平台迁移")
@RestController
@RequestMapping("/api/old/plat/move")
public class OpenMoveController {

  @ApiOperation("迁移用户相关")
  @PostMapping("/user")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'迁移用户相关'")
  public void moveUser() {}
}

package com.gameplat.admin.controller.open.common;

import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : cc @Author : cc @Date : 2022/4/17
 */
@Tag(name = "老平台迁移")
@RestController
@RequestMapping("/api/old/plat/move")
public class OpenMoveController {

  @Operation(summary = "迁移用户相关")
  @PostMapping("/user")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'迁移用户相关'")
  public void moveUser() {}
}

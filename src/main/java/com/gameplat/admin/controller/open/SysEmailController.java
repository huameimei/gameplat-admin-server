package com.gameplat.admin.controller.open;

import com.gameplat.admin.convert.SysEmailConvert;
import com.gameplat.admin.model.dto.SysEmailQueryDto;
import com.gameplat.admin.service.SysEmailService;
import com.gameplat.common.web.Result;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameplat-admin-service/api/internal/sysEmail")
public class SysEmailController {

  @Autowired private SysEmailService sysEmailService;

  @Autowired private SysEmailConvert sysEmailConvert;

  @GetMapping(value = "/queryAll")
  @ResponseBody
  public Result queryAll(SysEmailQueryDto sysEmailQueryDto) {
    return Result.succeed(
        sysEmailService.listByQueryDto(sysEmailQueryDto).stream()
            .map(i -> sysEmailConvert.toVo(i))
            .collect(Collectors.toList()));
  }
}

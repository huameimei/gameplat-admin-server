package com.gameplat.admin.controller.open;

import com.gameplat.admin.convert.SysSmsConvert;
import com.gameplat.admin.model.dto.SysSmsQueryDTO;
import com.gameplat.admin.service.SysSmsService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.common.web.Result;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/sysSms")
public class SysSmsController {

  @Autowired private SysSmsService sysSmsService;

  @Autowired private SysSmsConvert sysSmsConvert;

  @GetMapping(value = "/queryAll")
  public Result queryAll(SysSmsQueryDTO sysSmsQueryDto) {
    return Result.succeed(
        sysSmsService.listByQueryDto(sysSmsQueryDto).stream()
            .map(i -> sysSmsConvert.toVo(i))
            .collect(Collectors.toList()));
  }
}

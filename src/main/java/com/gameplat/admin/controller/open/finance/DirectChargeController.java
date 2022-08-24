package com.gameplat.admin.controller.open.finance;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.model.bean.DirectCharge;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "免提直充")
@RestController
@RequestMapping("/api/admin/finance/directCharge")
public class DirectChargeController {

  @Autowired private SysDictDataService dictDataService;

  @Operation(summary = "获取配置")
  @GetMapping("/get")
  public DirectCharge get() {
    SysDictData sysDictData = dictDataService.getDictData("system_config", "direct-charge");
    return JSON.parseObject(sysDictData.getDictValue(), DirectCharge.class);
  }

  @Operation(summary = "修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('finance:directCharge:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'免提直充->修改:' + #directCharge")
  public void update(@RequestBody DirectCharge directCharge) {
    SysDictData sysDictData = dictDataService.getDictData("system_config", "direct-charge");
    sysDictData.setDictValue(JSON.toJSONString(directCharge));
    dictDataService.updateById(sysDictData);
  }
}

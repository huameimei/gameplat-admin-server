package com.gameplat.admin.controller.open.finance;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.model.bean.DirectCharge;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "免提直充")
@RestController
@RequestMapping("/api/admin/finance/directCharge")
public class DirectChargeController {

  @Autowired private SysDictDataService dictDataService;

  @ApiOperation("获取配置")
  @GetMapping("/get")
  public DirectCharge get() {
    SysDictData sysDictData = dictDataService.getDictData("system_config", "direct-charge");
    return JSON.parseObject(sysDictData.getDictValue(), DirectCharge.class);
  }

  @ApiOperation("修改")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('finance:directCharge:edit')")
  public void update(@RequestBody DirectCharge directCharge) {
    SysDictData sysDictData = dictDataService.getDictData("system_config", "direct-charge");
    sysDictData.setDictValue(JSON.toJSONString(directCharge));
    dictDataService.updateById(sysDictData);
  }
}

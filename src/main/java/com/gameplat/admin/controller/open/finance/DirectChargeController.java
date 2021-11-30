package com.gameplat.admin.controller.open.finance;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.model.bean.DirectCharge;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/finance/directCharge")
public class DirectChargeController {

  @Autowired private SysDictDataService dictDataService;

  @GetMapping("/get")
  public DirectCharge get() {
    SysDictData sysDictData = dictDataService.getDictList("system_config", "direct-charge");
    return JSON.parseObject(sysDictData.getDictValue(), DirectCharge.class);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('finance:directCharge:edit')")
  public void update(@RequestBody DirectCharge directCharge) {
    SysDictData sysDictData = dictDataService.getDictList("system_config", "direct-charge");
    sysDictData.setDictValue(JSON.toJSONString(directCharge));
    dictDataService.updateById(sysDictData);
  }

}

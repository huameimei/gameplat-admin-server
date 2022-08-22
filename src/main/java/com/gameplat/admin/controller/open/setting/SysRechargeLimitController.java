package com.gameplat.admin.controller.open.setting;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.DictParamDTO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin/rechargelimit")
@Tag(name = "充值限制配置")
@RequiredArgsConstructor
public class SysRechargeLimitController {

  @Autowired private SysDictDataService dictDataService;

  @PreAuthorize("hasAuthority('system:recharge:edit')")
  @Operation(summary = "直播间发言限制信息修改")
  @PostMapping("/updaterechargelimit")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'充值限制配置-->直播间发言限制信息修改:' + #dictParamDTO")
  public void configDataEdit(@RequestBody DictParamDTO dictParamDTO) {
    dictDataService.batchUpdateDictData(dictParamDTO);
  }

  @Operation(summary = "获取直播间发言限制信息")
  @GetMapping(value = "/findData")
  // @PreAuthorize("hasAuthority('system:dict:view')")
  public JSONObject findData(@RequestParam(value = "dictType") String dictType) {
    return dictDataService.getData(dictType);
  }
}

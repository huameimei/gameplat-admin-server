package com.gameplat.admin.controller.open.setting;

import com.gameplat.admin.model.dto.DictParamDTO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bhf @Description 充值限制配置 @Date 2020/6/3 18:01
 */
@RestController
@RequestMapping(value = "/api/admin/rechargelimit")
@Api(tags = "充值限制配置")
@RequiredArgsConstructor
public class SysRechargeLimitController {

  @Autowired private SysDictDataService dictDataService;

  @PreAuthorize("hasAuthority('system:recharge:edit')")
  @ApiOperation(value = "直播间发言限制信息修改")
  @PostMapping("/updaterechargelimit")
  public Result configDataEdit(@RequestBody DictParamDTO dictParamDTO) {

    // 批量修改数据字典
    dictDataService.batchUpdateDictData(dictParamDTO);
    return Result.succeed();
  }

  @ApiOperation(value = "获取直播间发言限制信息")
  @GetMapping(value = "/findData")
  // @PreAuthorize("hasAuthority('system:dict:view')")
  public Result findData(@RequestParam(value = "dictType") String dictType) {
    return Result.succeedData(dictDataService.getData(dictType));
  }
}

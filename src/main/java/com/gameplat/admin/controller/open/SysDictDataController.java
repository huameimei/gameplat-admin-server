package com.gameplat.admin.controller.open;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.web.Result;
import com.gameplat.ds.core.context.DyDataSourceContextHolder;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameplat-admin-service/api/open/dictData")
public class SysDictDataController {

  @Autowired
  private SysDictDataService sysDictDataService;

  @RequestMapping(value = "/get/{dictType}", method = RequestMethod.GET)
  public Result get(@PathVariable String dictType) throws Exception {
    return Result.succeed(sysDictDataService.selectDictDataListByType(dictType)
        .stream().collect(Collectors.toMap(SysDictData::getDictLabel,SysDictData::getDictValue)));
  }

  @ApiOperation(value = "获取数据字典静态目录")
  @GetMapping("/get_folder")
  public Result getFolder() {
    try {
      String folder = "tenant_" + DyDataSourceContextHolder.getDBSuffix();
      if (StringUtils.isBlank(folder)) {
        return Result.failed("获取不到！");
      }
      JSONObject json = new JSONObject();
      json.put("folder", folder);
      return Result.succeed(json);
    } catch (Exception e) {
      return Result.failed("操作失败");
    }
  }




}

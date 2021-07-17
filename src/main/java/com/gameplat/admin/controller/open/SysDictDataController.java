package com.gameplat.admin.controller.open;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.model.dto.SysDictDataAddDTO;
import com.gameplat.admin.model.dto.SysDictDataEditDTO;
import com.gameplat.admin.model.dto.SysDictDataQueryDTO;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.utils.JsonFileUtil;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.common.web.Result;
import com.gameplat.ds.core.context.DyDataSourceContextHolder;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.API + "/dictData")
public class SysDictDataController {

  @Autowired private SysDictDataService sysDictDataService;

  @GetMapping(value = "/queryAll")
  @ResponseBody
  public IPage<SysDictDataVO> queryAll(IPage<SysDictData> page, SysDictDataQueryDTO queryDto) {
    return sysDictDataService.queryPage(page, queryDto);
  }

  @PostMapping(value = "/save")
  @ResponseBody
  public void save(SysDictDataAddDTO sysDictDataAddDto) {
    sysDictDataService.save(sysDictDataAddDto);
  }

  @PostMapping(value = "/update")
  @ResponseBody
  public void update(SysDictDataEditDTO sysDictDataEditDto) {
    sysDictDataService.update(sysDictDataEditDto);
  }

  @DeleteMapping(value = "/delete/{id}")
  @ResponseBody
  public void delete(@PathVariable("id") Long id) {
    sysDictDataService.delete(id);
  }

  @RequestMapping(value = "/getAdminLoginConfig", method = RequestMethod.GET)
  public Result getAdminLoginConfig() throws Exception {
    return Result.succeed(
        sysDictDataService
            .selectDictDataListByType(DictTypeEnum.ADMIN_LOGIN_CONFIG.getValue())
            .stream()
            .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue)));
  }

  @RequestMapping(value = "/getDictData/{dictType}", method = RequestMethod.GET)
  public Result get(@PathVariable String dictType) throws Exception {
    String prefix =
        sysDictDataService.getDefaultJson()
            + Constants.TENANT
            + DyDataSourceContextHolder.getDBSuffix();
    JSONObject json = JsonFileUtil.getJson(prefix, dictType);
    if (json == null) {
      return Result.succeed(
          sysDictDataService.selectDictDataListByType(dictType).stream()
              .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue)));
    }
    return Result.succeed(json);
  }
}

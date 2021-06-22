package com.gameplat.admin.controller.open;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.model.dto.SysDictDataAddDto;
import com.gameplat.admin.model.dto.SysDictDataEditDto;
import com.gameplat.admin.model.dto.SysDictDataQueryDto;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.web.Result;
import com.gameplat.ds.core.context.DyDataSourceContextHolder;
import io.swagger.annotations.ApiOperation;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/dictData")
public class SysDictDataController {

  @Autowired
  private SysDictDataService sysDictDataService;


  @GetMapping(value = "/queryAll")
  @ResponseBody
  public IPage<SysDictDataVo> queryAll(IPage<SysDictData> page, SysDictDataQueryDto queryDto) {
    return sysDictDataService.queryPage(page,queryDto);
  }


  @PostMapping(value = "/save")
  @ResponseBody
  public void save(SysDictDataAddDto sysDictDataAddDto){
    sysDictDataService.save(sysDictDataAddDto);
  }

  @PostMapping(value = "/update")
  @ResponseBody
  public void update(SysDictDataEditDto sysDictDataEditDto){
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
        sysDictDataService.selectDictDataListByType(DictTypeEnum.ADMIN_LOGIN_CONFIG.getValue()).stream()
            .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue)));
  }

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

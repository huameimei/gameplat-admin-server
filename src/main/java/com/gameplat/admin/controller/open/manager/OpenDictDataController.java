package com.gameplat.admin.controller.open.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.group.Groups;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/dict/data")
public class OpenDictDataController {

  @Autowired private SysDictDataService dictDataService;

  @ApiOperation("查询")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public IPage<DictDataVo> page(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    return dictDataService.selectDictDataList(page, dictData);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:dict:add')")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictDataDTO dto) {
    dictDataService.insertDictData(dto);
  }

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:dict:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictDataDTO dto) {
    dictDataService.updateDictData(dto);
  }

  @ApiOperation("删除")
  @PostMapping("/delete/{ids}")
  @PreAuthorize("hasAuthority('system:dict:remove')")
  public void remove(@PathVariable List<Long> ids) {
    dictDataService.deleteDictDataByIds(ids);
  }

  @ApiOperation("获取全部")
  @GetMapping("/listAll")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public List<SysDictData> list(SysDictDataDTO dictData) {
    return dictDataService.getDictListAll(dictData);
  }
}

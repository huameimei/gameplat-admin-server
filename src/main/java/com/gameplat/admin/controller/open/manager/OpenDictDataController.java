package com.gameplat.admin.controller.open.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典管理")
@RestController
@RequestMapping("/api/admin/manager/dict/data")
public class OpenDictDataController {

  @Autowired private SysDictDataService dictDataService;

  @Operation(summary = "查询")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public IPage<DictDataVo> page(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    return dictDataService.selectDictDataList(page, dictData);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:dict:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->添加' + #dto" )
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictDataDTO dto) {
    dictDataService.insertDictData(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:dict:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->编辑' + #dto" )
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictDataDTO dto) {
    dictDataService.updateDictData(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{ids}")
  @PreAuthorize("hasAuthority('system:dict:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->删除' + #ids" )
  public void remove(@PathVariable List<Long> ids) {
    dictDataService.deleteDictDataByIds(ids);
  }

  @Operation(summary = "获取全部")
  @GetMapping("/listAll")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public List<SysDictData> list(SysDictDataDTO dictData) {
    return dictDataService.getDictListAll(dictData);
  }
}

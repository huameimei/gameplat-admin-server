package com.gameplat.admin.controller.open.manager;

import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典类型管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/dict")
public class OpenDictTypeController {

  @Autowired private SysDictTypeService dictTypeService;

  @Operation(summary = "查询")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public List<DictTypeVO> page(OperDictTypeDTO dto) {
    return dictTypeService.selectDictTypeList(dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:dict:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->添加' + #dto" )
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.addDictType(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:dict:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->编辑' + #dto" )
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.editDictType(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('system:dict:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'字典管理-->删除' + #id" )
  public void remove(@PathVariable Long id) {
    dictTypeService.removeDictTypeById(id);
  }
}

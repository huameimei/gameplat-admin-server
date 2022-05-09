package com.gameplat.admin.controller.open.manager;

import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.common.group.Groups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典类型管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/dict")
public class OpenDictTypeController {

  @Autowired private SysDictTypeService dictTypeService;

  @ApiOperation("查询")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public List<DictTypeVO> page(OperDictTypeDTO dto) {
    return dictTypeService.selectDictTypeList(dto);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:dict:add')")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.addDictType(dto);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:dict:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.editDictType(dto);
  }

  @ApiOperation("删除")
  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('system:dict:remove')")
  public void remove(@PathVariable Long id) {
    dictTypeService.removeDictTypeById(id);
  }
}

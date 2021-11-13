package com.gameplat.admin.controller.open.manager;

import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.common.group.Groups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/manager/dict")
public class OpenDictTypeController {

  @Autowired private SysDictTypeService dictTypeService;

  @GetMapping("/page")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public List<DictTypeVO> page(OperDictTypeDTO dto) {
    return dictTypeService.selectDictTypeList(dto);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:dict:add')")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.addDictType(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:dict:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictTypeDTO dto) {
    dictTypeService.editDictType(dto);
  }

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('system:dict:remove')")
  public void remove(@PathVariable Long id) {
    dictTypeService.removeDictTypeById(id);
  }
}

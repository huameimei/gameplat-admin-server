package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.group.Groups;
import com.gameplat.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin/thirdParty/rechBank")
public class RechBankController {

  @Autowired private SysDictDataService dictDataService;

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:delete')")
  public void remove(@RequestBody List<Long> ids) {
    dictDataService.deleteDictDataByIds(ids);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:add')")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictDataDTO dictData) {
    dictDataService.insertDictData(dictData);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:edit')")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictDataDTO dictData) {
    dictDataService.updateDictData(dictData);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:editStatus')")
  public void updateStatus(Long id, Integer status) {
    dictDataService.updateStatus(id, status);
  }

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:list')")
  public IPage<DictDataVo> list(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    return dictDataService.selectDictDataList(page, dictData);
  }

  @GetMapping("/getAllBank")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:getAllBank')")
  public List<SysDictData> getAllBank() {
    SysDictData sysDictData = new SysDictData();
    sysDictData.setDictType("RECH_BANK");
    return dictDataService.getDictList(sysDictData);
  }
}

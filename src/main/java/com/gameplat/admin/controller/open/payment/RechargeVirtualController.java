package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "虚拟币")
@RestController
@RequestMapping("/api/admin/thirdParty/rechVirtual")
public class RechargeVirtualController {

  @Autowired private SysDictDataService dictDataService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:view')")
  public IPage<DictDataVo> list(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    dictData.setDictType(DictTypeEnum.RECH_VIRTUAL.getValue());
    return dictDataService.selectDictDataList(page, dictData);
  }

  @Operation(summary = "修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'虚拟币-->修改:' + #dictData")
  public void update(@RequestBody OperDictDataDTO dictData) {
    if (StringUtils.isNull(dictData.getId())) {
      throw new ServiceException("主键不正确");
    }
    if (StringUtils.isBlank(dictData.getDictValue())) {
      throw new ServiceException("值不能为空");
    }
    dictData.setDictType(DictTypeEnum.RECH_VIRTUAL.getValue());
    dictDataService.updateDictData(dictData);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'虚拟币-->添加:' + #dictData")
  public void add(@RequestBody OperDictDataDTO dictData) {
    if (StringUtils.isBlank(dictData.getDictValue())) {
      throw new ServiceException("值不能为空");
    }
    dictData.setDictType(DictTypeEnum.RECH_VIRTUAL.getValue());
    dictDataService.insertDictData(dictData);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'虚拟币-->删除:' + #id")
  public void remove(@PathVariable Long id) {
    dictDataService.removeById(id);
  }

  @Operation(summary = "修改状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:editStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'虚拟币-->修改状态:' + #status")
  public void updateStatus(Long id, Integer status) {
    dictDataService.updateStatus(id, status);
  }
}

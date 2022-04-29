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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "虚拟币")
@RestController
@RequestMapping("/api/admin/thirdParty/rechVirtual")
public class RechargeVirtualController {

  @Autowired private SysDictDataService dictDataService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:view')")
  public IPage<DictDataVo> list(PageDTO<SysDictData> page, SysDictDataDTO dictData) {
    dictData.setDictType(DictTypeEnum.RECH_VIRTUAL.getValue());
    return dictDataService.selectDictDataList(page, dictData);
  }

  @ApiOperation("修改")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'修改虚拟币id=' + #dictData.id")
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

  @ApiOperation("添加")
  @PutMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增虚拟币id=' + #dictData.id")
  public void add(@RequestBody OperDictDataDTO dictData) {
    if (StringUtils.isBlank(dictData.getDictValue())) {
      throw new ServiceException("值不能为空");
    }
    dictData.setDictType(DictTypeEnum.RECH_VIRTUAL.getValue());
    dictDataService.insertDictData(dictData);
  }

  @ApiOperation("删除")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除虚拟币id=' + #id")
  public void remove(@PathVariable Long id) {
    dictDataService.removeById(id);
  }

  @ApiOperation("修改状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:rechVirtual:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改虚拟币状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    dictDataService.updateStatus(id, status);
  }
}

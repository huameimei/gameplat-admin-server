package com.gameplat.admin.controller.open.payment;

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

@Tag(name = "银行卡")
@RestController
@RequestMapping("/api/admin/thirdParty/rechBank")
public class RechargeBankController {

  @Autowired private SysDictDataService dictDataService;

  @Operation(summary = "删除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'删除银行ids=' + #ids")
  public void remove(@RequestBody List<Long> ids) {
    dictDataService.deleteDictDataByIds(ids);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'新增银行id=' + #dictData.id")
  public void save(@Validated(Groups.INSERT.class) @RequestBody OperDictDataDTO dictData) {
    dictDataService.insertBank(dictData);
  }

  @Operation(summary = "修改")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.WITHDRAW,
      desc = "'修改银行id=' + #dictData.id")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody OperDictDataDTO dictData) {
    dictDataService.updateDictData(dictData);
  }

  @Operation(summary = "编辑状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.WITHDRAW, desc = "'修改银行状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    dictDataService.updateStatus(id, status);
  }

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('thirdParty:rechBank:view')")
  public IPage<DictDataVo> list(PageDTO<SysDictData> page, SysDictDataDTO dto) {
    return dictDataService.selectDictDataList(page, dto);
  }

  @Operation(summary = "获取全部")
  @GetMapping("/getAllBank")
  //  @PreAuthorize("hasAuthority('thirdParty:rechBank:getAllBank')")
  public List<SysDictData> getAllBank() {
    SysDictData sysDictData = new SysDictData();
    sysDictData.setDictType("RECH_BANK");
    return dictDataService.getDictList(sysDictData);
  }
}

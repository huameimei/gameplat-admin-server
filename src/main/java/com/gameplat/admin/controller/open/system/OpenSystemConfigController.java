package com.gameplat.admin.controller.open.system;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/system/config")
public class OpenSystemConfigController {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private DictDataConvert dictDataConvert;

  @Autowired private SystemConfigService systemConfigService;

  @GetMapping("/listAll")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public JSONObject listAll(SysDictDataDTO dictDataDTO) {
    SysDictData sysDictData = dictDataConvert.toEntity(dictDataDTO);
    List<SysDictData> list = dictDataService.getDictList(sysDictData);
    JSONObject json = new JSONObject();
    for (SysDictData data : list) {
      json.set(data.getDictLabel(), data.getDictValue());
    }
    return json;
  }

  @GetMapping("/sms/list")
  public List<SysSmsConfigVO> findSmsList() {
    return systemConfigService.findSmsList();
  }

  @GetMapping("/file/list")
  public List<SysFileConfigVO> findFileList() {
    return systemConfigService.findFileList();
  }

  @PutMapping("/sms/update")
  public void updateSms(@RequestBody SysSmsConfigDTO sysSmsConfigDTO) {
    systemConfigService.updateSmsConfig(sysSmsConfigDTO);
  }

  @PutMapping("/file/update")
  public void updateSms(@RequestBody SysFileConfigDTO sysFileConfigDTO) {
    systemConfigService.updateFileConfig(sysFileConfigDTO);
  }

  @PutMapping("/update")
  public void configDataEdit(@RequestBody OperSystemConfigDTO dto) {
    systemConfigService.configDataEdit(dto);
  }

  @GetMapping("/smsArea/list")
  public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SysSmsAreaQueryDTO dto) {
    return systemConfigService.findSmsAreaList(page, dto);
  }

  @PutMapping("/smsArea/update")
  public void smsAreaEdit(@RequestBody OperSysSmsAreaDTO dto) {
    systemConfigService.smsAreaEdit(dto);
  }

  @DeleteMapping("/smsArea/del/{id}")
  public void smsAreaEdit(@PathVariable("id") Long id) {
    systemConfigService.smsAreaDelete(id);
  }
}

package com.gameplat.admin.controller.open.system;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysEmail;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;

import java.util.ArrayList;
import java.util.List;

import com.gameplat.common.model.bean.EmailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    for (SysDictData dictData : list) {
      JSONArray dictValue= JSONArray.parseArray(dictData.getDictValue());
      json.set(dictData.getDictLabel(), dictValue);
    }
    return json;
  }

  @GetMapping("/agent/list")
  public List<AgentContacaVO> findAgentContacaList() {
    return systemConfigService.findAgentContacaList();
  }

  @PutMapping("/agent/update")
  public void updateAgentContaca(@RequestBody AgentContacaDTO agentContacaDTO) {
    systemConfigService.updateAgentContaca(agentContacaDTO);
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

  @GetMapping("/email/list")
  public EmailConfig findEmailList() {
    return systemConfigService.findEmailConfig();
  }

  @PutMapping("/email/update")
  public void updateEmail(@RequestBody EmailConfig emailConfig) {
    systemConfigService.updateEmail(emailConfig);
  }
}

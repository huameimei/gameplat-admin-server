package com.gameplat.admin.controller.open.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.gameplat.admin.convert.DictDataConvert;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.AgentContacaDTO;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.admin.model.dto.OperSystemConfigDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.common.model.bean.EmailConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "系统配置API")
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
    if (CollectionUtil.isNotEmpty(list)) {
      list.forEach(data -> json.set(data.getDictLabel(), data.getDictValue()));
    }

    return json;
  }

  @ApiOperation(value = "代理联系方式地址列表")
  @GetMapping("/agent/list")
  public List<AgentContacaVO> findAgentContacaList() {
    return systemConfigService.findAgentContacaList();
  }

  @ApiOperation(value = "编辑、新增代理联系方式地址")
  @PutMapping("/agent/edit")
  public void updateAgentContact(@RequestBody AgentContacaDTO agentContacaDTO) {
    systemConfigService.updateAgentContaca(agentContacaDTO);
  }

  @ApiOperation(value = "删除代理联系方式地址")
  @DeleteMapping("/agent/del/{id}")
  public void delAgentContaca(@PathVariable("id") Long id) {
    systemConfigService.delAgentContaca(id);
  }

  @GetMapping("/list/{dictType}")
  public List<SysDictData> findList(@PathVariable String dictType) {
    return systemConfigService.findList(dictType);
  }

  @PutMapping("/update/{dictType}")
  public void updateConfig(@PathVariable String dictType, @RequestBody SysDictData dictData) {
    systemConfigService.updateConfig(dictData);
  }

  @PutMapping("/update")
  public void configDataEdit(@RequestBody OperSystemConfigDTO dto) {
    systemConfigService.configDataEdit(dto);
  }

  @GetMapping("/email/list")
  public EmailConfig findEmailList() {
    return systemConfigService.findEmailConfig();
  }

  @PutMapping("/email/update")
  public void updateEmail(@RequestBody EmailConfig emailConfig) {
    systemConfigService.updateEmail(emailConfig);
  }

  @PostMapping("/email/testSend")
  public void testSendEmail(@RequestBody EmailTestDTO dto) {
    systemConfigService.testSendEmail(dto);
  }
}

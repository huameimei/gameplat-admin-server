package com.gameplat.admin.controller.open.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.model.dto.AgentContactDTO;
import com.gameplat.admin.model.dto.DictParamDTO;
import com.gameplat.admin.model.dto.DirectChargeDto;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.admin.model.vo.DirectChargeVo;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.model.bean.*;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "系统配置API")
@RequestMapping("/api/admin/system/config")
public class OpenSystemConfigController {

  @Autowired
  private SysDictDataService dictDataService;

  @Autowired
  private SystemConfigService systemConfigService;

  @Autowired
  private ConfigService configService;

  @Operation(summary = "获取系统配置")
  @GetMapping("/system")
  @PreAuthorize("hasAuthority('system:config:view:system')")
  public SystemConfig getSystemConfig() {
    return configService.get(DictTypeEnum.SYSTEM_PARAMETER_CONFIG, SystemConfig.class);
  }

  @Operation(summary = "修改系统配置")
  @PostMapping("/update/system")
  @PreAuthorize("hasAuthority('system:config:update:system')")
  public void updateSystemConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.SYSTEM_PARAMETER_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取借呗配置")
  @GetMapping("/loan")
  @PreAuthorize("hasAuthority('system:config:view:loan')")
  public LoanConfig getLoanConfig() {
    return configService.get(DictTypeEnum.LOAN_CONFIG, LoanConfig.class);
  }

  @Operation(summary = "修改借呗配置")
  @PostMapping("/update/loan")
  @PreAuthorize("hasAuthority('system:config:update:loan')")
  public void updateLoanConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.LOAN_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取余额宝配置")
  @GetMapping("/yubao")
  @PreAuthorize("hasAuthority('system:config:view:yubao')")
  public YubaoConfig getYuBaoConfig() {
    return configService.get(DictTypeEnum.YUBAO_CONFIG, YubaoConfig.class);
  }

  @Operation(summary = "修改余额宝配置")
  @PostMapping("/update/yubao")
  @PreAuthorize("hasAuthority('system:config:update:yubao')")
  public void updateYuBaoConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.YUBAO_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取短信配置")
  @GetMapping("/sms")
  @PreAuthorize("hasAuthority('system:config:view:sms')")
  public List<SysDictData> getSmsConfig() {
    return dictDataService.getDictDataByType(DictTypeEnum.SMS_CONFIG.getValue());
  }

  @Operation(summary = "修改短信配置")
  @PostMapping("/update/sms")
  @PreAuthorize("hasAuthority('system:config:update:sms')")
  public void updateSmsConfig(@RequestBody List<SysDictData> dictDataList) {
    systemConfigService.updateConfig(DictTypeEnum.SMS_CONFIG.getValue(), dictDataList);
  }

  @Operation(summary = "获取文件配置")
  @GetMapping("/file")
  @PreAuthorize("hasAuthority('system:config:view:file')")
  public List<SysDictData> getFileConfig() {
    return dictDataService.getDictDataByType(DictTypeEnum.FILE_CONFIG.getValue());
  }

  @Operation(summary = "修改文件配置")
  @PostMapping("/update/file")
  @PreAuthorize("hasAuthority('system:config:update:file')")
  public void updateFileConfig(@RequestBody List<SysDictData> dictDataList) {
    systemConfigService.updateConfig(DictTypeEnum.FILE_CONFIG.getValue(), dictDataList);
  }

  @Operation(summary = "获取验证码配置")
  @GetMapping("/captcha")
  @PreAuthorize("hasAuthority('system:config:view:captcha')")
  public List<SysDictData> getCaptchaConfig() {
    return dictDataService.getDictDataByType(DictTypeEnum.VERIFICATION_CODE_CONFIG.getValue());
  }

  @Operation(summary = "修改验证码配置")
  @PostMapping("/update/captcha")
  @PreAuthorize("hasAuthority('system:config:update:captcha')")
  public void updateCaptchaConfig(@RequestBody List<SysDictData> dictDataList) {
    systemConfigService.updateConfig(
            DictTypeEnum.VERIFICATION_CODE_CONFIG.getValue(), dictDataList);
  }

  @Operation(summary = "获取邮箱配置")
  @GetMapping("/email")
  @PreAuthorize("hasAuthority('system:config:view:email')")
  public EmailConfig getEmailConfig() {
    return configService.get(DictTypeEnum.EMAIL_CONFIG, EmailConfig.class);
  }

  @Operation(summary = "修改邮箱配置")
  @PostMapping("/update/email")
  @PreAuthorize("hasAuthority('system:config:update:email')")
  public void updateEmailConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.EMAIL_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取代理后台设置")
  @GetMapping("/agent/backend")
  @PreAuthorize("hasAuthority('system:config:view:agentBackend')")
  public AgentBackendConfig getAgentBackendConfig() {
    return configService.get(DictTypeEnum.AGENT_BACKEND_CONFIG, AgentBackendConfig.class);
  }

  @Operation(summary = "修改代理后台配置")
  @PostMapping("/update/agentBackend")
  @PreAuthorize("hasAuthority('system:config:update:agentBackend')")
  public void updateAgentBackendConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.AGENT_BACKEND_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取活动模板配置")
  @GetMapping("/activityTemplate")
  @PreAuthorize("hasAuthority('system:config:view:activityTemplate')")
  public ActivityTemplateConfig getActivityTemplateConfig() {
    return configService.get(DictTypeEnum.ACTIVITY_TEMPLATE_CONFIG, ActivityTemplateConfig.class);
  }

  @Operation(summary = "修改活动模板")
  @PostMapping("/update/activityTemplate")
  @PreAuthorize("hasAuthority('system:config:update:activityTemplate')")
  public void updateActivityTemplateConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.ACTIVITY_TEMPLATE_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取红包模板配置")
  @GetMapping("/redPacketTemplate")
  @PreAuthorize("hasAuthority('system:config:view:redPacketTemplate')")
  public RedPacketTemplateConfig getRedPacketTemplate() {
    return configService.get(DictTypeEnum.RED_PACKET_CONFIG, RedPacketTemplateConfig.class);
  }

  @Operation(summary = "修改红包模板配置")
  @PostMapping("/update/readPacketTemplate")
  @PreAuthorize("hasAuthority('system:config:update:readPacketTemplate')")
  public void updateRedPacketTemplateConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.RED_PACKET_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取身份认证配置")
  @GetMapping("/idAuth")
  @PreAuthorize("hasAuthority('system:config:view:idAuth')")
  public IDAuthenticationConfig getIdAuthConfig() {
    return configService.get(DictTypeEnum.ID_AUTHENTICATION_CONFIG, IDAuthenticationConfig.class);
  }

  @Operation(summary = "修改身份认证配置")
  @PostMapping("/update/idAuth")
  @PreAuthorize("hasAuthority('system:config:update:idAuth')")
  public void updateIdAuth(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.ID_AUTHENTICATION_CONFIG.getValue(), param);
  }

  @Operation(summary = "获取维护时间配置")
  @GetMapping("/maintain")
  @PreAuthorize("hasAuthority('system:config:view:maintain')")
  public String getMaintainConfig() {
    return configService.getValue(DictDataEnum.SYSTEM_MAINTAIN_TIME);
  }

  @Operation(summary = "修改维护时间配置")
  @PostMapping("/update/maintain")
  @PreAuthorize("hasAuthority('system:config:update:maintain')")
  public void updateMaintainConfig(@RequestBody Map<String, Object> param) {
    systemConfigService.updateConfig(DictTypeEnum.MAINTAIN_TIME_CONFIG.getValue(), param);
  }

  @GetMapping("/getConfig/{dictType}/{dictLabel}")
  @PreAuthorize("hasAuthority('system:dict:view')")
  public SysDictData getConfig(@PathVariable String dictType, @PathVariable String dictLabel) {
    return dictDataService.getDictData(dictType, dictLabel);
  }

  @Operation(summary = "代理联系方式地址列表")
  @GetMapping("/agentContact")
  @PreAuthorize("hasAuthority('system:config:agentContcat:view')")
  public List<AgentContactConfig> getAgentContact() {
    return configService.get(
            DictDataEnum.AGENT_CONTACT, new TypeReference<List<AgentContactConfig>>() {
            });
  }

  @Operation(summary = "编辑、新增代理联系方式地址")
  @PostMapping("/agentContact/edit")
  @PreAuthorize("hasAuthority('system:config:agentContcat:edit')")
  public void updateAgentContact(@RequestBody AgentContactDTO dto) {
    systemConfigService.updateAgentContact(dto);
  }

  @Operation(summary = "删除代理联系方式地址")
  @PostMapping("/agentContact/del/{id}")
  @PreAuthorize("hasAuthority('system:config:agentContcat:remove')")
  public void delAgentContact(@PathVariable("id") String id) {
    systemConfigService.delAgentContact(id);
  }

  @GetMapping("/list/{dictType}")
  @PreAuthorize("hasAuthority('system:config:view')")
  public List<SysDictData> findList(@PathVariable String dictType) {
    return systemConfigService.findList(dictType);
  }

  @PostMapping("/update/{dictType}")
  @PreAuthorize("hasAuthority('system:config:add')")
  public void updateConfig(
          @PathVariable String dictType, @RequestBody List<SysDictData> dictDataList) {
    systemConfigService.updateConfig(dictType, dictDataList);
  }

  @PostMapping("/email/testSend")
  public void testSendEmail(@RequestBody EmailTestDTO dto) {
    systemConfigService.testSendEmail(dto);
  }


  @Operation(summary = "获取免提直冲限制")
  @GetMapping(value = "/get/directCharge")
  @PreAuthorize("hasAuthority('system:directCharge:get')")
  public DirectChargeVo directCharge() {
    String dictDataValue = dictDataService.getDirectChargeValue(DictDataEnum.DIRECT_CHARGE.getType().getValue(),
            DictDataEnum.DIRECT_CHARGE.getLabel());
    return JSON.parseObject(dictDataValue, DirectChargeVo.class);
  }

  @Operation(summary = "保存免提直冲")
  @PostMapping(value = "/add/directCharge")
  @PreAuthorize("hasAuthority('system:directCharge:add')")
  public void addDirectCharge(@RequestBody DirectChargeDto directChargeDto) {
    String directCharge = JSON.toJSONString(directChargeDto);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(DictDataEnum.DIRECT_CHARGE.getLabel(), directCharge);
    DictParamDTO dictParamDTO = new DictParamDTO();
    dictParamDTO.setJsonData(jsonObject);
    dictParamDTO.setDictType(DictDataEnum.DIRECT_CHARGE.getType().getValue());
    dictDataService.batchUpdateDictData(dictParamDTO);
  }
}

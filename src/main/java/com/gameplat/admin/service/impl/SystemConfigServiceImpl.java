package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.AgentContacaConfigConvert;
import com.gameplat.admin.convert.SysFileConfigConvert;
import com.gameplat.admin.convert.SysSmsAreaConvert;
import com.gameplat.admin.convert.SysSmsConfigConvert;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.model.bean.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SystemConfigServiceImpl implements SystemConfigService {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private ConfigService configService;

  @Autowired private SysSmsConfigConvert sysSmsConfigConvert;

  @Autowired private SysFileConfigConvert sysFileConfigConvert;

  @Autowired private SysSmsAreaService sysSmsAreaService;

  @Autowired private SysSmsAreaConvert sysSmsAreaConvert;

  @Autowired private AgentContacaConfigConvert agentContacaConfigConvert;

  @Override
  public List<AgentContacaVO> findAgentContacaList() {
    String dictData = configService.getValue(DictDataEnum.AGENT_CONTACT_CONFIG);
    if (StringUtils.isNotBlank(dictData)) {
      return JSONArray.parseArray(dictData, AgentContacaVO.class);
    }

    return Collections.emptyList();
  }

  @Override
  public void updateAgentContaca(AgentContacaDTO dto) {
    SysDictData dictData =
            dictDataService.getDictData(
                    DictTypeEnum.AGENT_CONTACT.getValue(), DictDataEnum.AGENT_CONTACT_CONFIG.getLabel());

    List<AgentContacaConfig> agentContacaConfigList =
            Optional.of(dictData)
                    .map(SysDictData::getDictValue)
                    .map(c -> JsonUtils.parse(c, new TypeReference<List<AgentContacaConfig>>() {}))
                    .orElse(Collections.emptyList());

    AgentContacaConfig agentContacaConfig = agentContacaConfigConvert.toEntity(dto);
    List<AgentContacaConfig> list = new ArrayList<>();

    Boolean flag = false;
    if (CollectionUtils.isNotEmpty(agentContacaConfigList)) {
        for (AgentContacaConfig contacaConfig : agentContacaConfigList) {
            if(contacaConfig.getId().equals(dto.getId())){
              flag = true;
              agentContacaConfig.setUpdateBy(GlobalContextHolder.getContext().getUsername());
              agentContacaConfig.setUpdateTime(String.valueOf(System.currentTimeMillis()));
              list.add(agentContacaConfig);
            }else{ //新增
                list.add(contacaConfig);
            }
        }
        if (!flag){
          agentContacaConfig.setId(IdWorker.getId());
          agentContacaConfig.setCreateBy(GlobalContextHolder.getContext().getUsername());
          agentContacaConfig.setCreateTime(String.valueOf(System.currentTimeMillis()));
          list.add(agentContacaConfig);
        }

      // 修改
      dictDataService.updateDictData(
              OperDictDataDTO.builder()
                      .id(dictData.getId())
                      .dictLabel(dictData.getDictLabel())
                      .dictType(dictData.getDictType())
                      .dictValue(JsonUtils.toJson(list))
                      .build());
    }
  }

  @Override
  public void delAgentContaca(Long id) {
    List<AgentContacaConfig> list = new ArrayList<>();

    SysDictData dictData =
            dictDataService.getDictData(
                    DictTypeEnum.AGENT_CONTACT.getValue(), DictDataEnum.AGENT_CONTACT_CONFIG.getLabel());

    List<AgentContacaConfig> agentContacaConfigList =
            Optional.of(dictData)
                    .map(SysDictData::getDictValue)
                    .map(c -> JsonUtils.parse(c, new TypeReference<List<AgentContacaConfig>>() {}))
                    .orElse(Collections.emptyList());
    if (CollectionUtils.isNotEmpty(agentContacaConfigList)){
      for (AgentContacaConfig agentContacaConfig : agentContacaConfigList) {
        if(!agentContacaConfig.getId().equals(id)){
          list.add(agentContacaConfig);
        }
      }
      dictDataService.updateDictData(
              OperDictDataDTO.builder()
                      .id(dictData.getId())
                      .dictLabel(dictData.getDictLabel())
                      .dictType(dictData.getDictType())
                      .dictValue(JsonUtils.toJson(list))
                      .build());
    }
  }

  @Override
  public List<SysSmsConfigVO> findSmsList() {
    String dictData = configService.getValue(DictDataEnum.SMS);
    if (StringUtils.isNotBlank(dictData)) {
      return JSONArray.parseArray(dictData, SysSmsConfigVO.class);
    }

    return Collections.emptyList();
  }

  @Override
  public List<SysFileConfigVO> findFileList() {
    String dictData = configService.getValue(DictDataEnum.FILE);
    if (StringUtils.isNotBlank(dictData)) {
      return JSONArray.parseArray(dictData, SysFileConfigVO.class);
    }
    return Collections.emptyList();
  }

  @Override
  public void updateSmsConfig(SysSmsConfigDTO dto) {
    // 查询短信配置
    SysDictData dictData =
        dictDataService.getDictData(
            DictTypeEnum.SMS_CONFIG.getValue(), DictDataEnum.SMS.getLabel());

    List<SysSmsConfig> smsConfigList =
        Optional.of(dictData)
            .map(SysDictData::getDictValue)
            .map(c -> JsonUtils.parse(c, new TypeReference<List<SysSmsConfig>>() {}))
            .orElse(Collections.emptyList());

    SysSmsConfig smsConfig = sysSmsConfigConvert.toEntity(dto);
    if (CollectionUtils.isNotEmpty(smsConfigList)) {
      // 替换旧数据
      smsConfigList.replaceAll(
          c -> c.getOperator().equals(smsConfig.getOperator()) ? smsConfig : c);

      // 修改
      dictDataService.updateDictData(
          OperDictDataDTO.builder()
              .id(dictData.getId())
              .dictLabel(dictData.getDictLabel())
              .dictType(dictData.getDictType())
              .dictValue(JsonUtils.toJson(smsConfigList))
              .build());
    }
  }

  @Override
  public void updateFileConfig(SysFileConfigDTO dto) {
    SysDictData dictData =
        dictDataService.getDictData(
            DictTypeEnum.FILE_CONFIG.getValue(), DictDataEnum.FILE.getLabel());

    List<SysFileConfig> fileConfigList =
        Optional.of(dictData)
            .map(SysDictData::getDictValue)
            .map(c -> JsonUtils.parse(c, new TypeReference<List<SysFileConfig>>() {}))
            .orElse(Collections.emptyList());

    SysFileConfig config = sysFileConfigConvert.toEntity(dto);
    if (CollectionUtils.isNotEmpty(fileConfigList)) {
      // 替换旧数据
      fileConfigList.replaceAll(
          c -> c.getServiceProvider().equals(config.getServiceProvider()) ? config : c);

      dictDataService.updateDictData(
          OperDictDataDTO.builder()
              .id(dictData.getId())
              .dictLabel(dictData.getDictLabel())
              .dictType(dictData.getDictType())
              .dictValue(JsonUtils.toJson(fileConfigList))
              .build());
    }
  }

  @Override
  public void configDataEdit(OperSystemConfigDTO dto) {
    JSONObject json = dto.getJsonData();
    // json遍历
    for (Map.Entry<String, Object> entry : json.entrySet()) {
      SysDictData sysDictData = new SysDictData();
      sysDictData.setDictType(dto.getDictType());
      sysDictData.setDictLabel(entry.getKey());
      sysDictData.setDictValue(entry.getValue().toString());

      LambdaQueryWrapper<SysDictData> queryWrapper = Wrappers.lambdaQuery();
      queryWrapper
          .eq(SysDictData::getDictType, dto.getDictType())
          .eq(SysDictData::getDictLabel, entry.getKey());
      if (!dictDataService.update(sysDictData, queryWrapper)) {
        throw new ServiceException("更新配置失败!");
      }
    }
  }

  @Override
  public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SysSmsAreaQueryDTO dto) {
    LambdaQueryWrapper<SysSmsArea> query = Wrappers.lambdaQuery();
    query
        .eq(ObjectUtils.isNotEmpty(dto.getCode()), SysSmsArea::getCode, dto.getCode())
        .eq(ObjectUtils.isNotEmpty(dto.getName()), SysSmsArea::getName, dto.getName());
    return sysSmsAreaService.page(page, query).convert(sysSmsAreaConvert::toVo);
  }

  @Override
  public void smsAreaEdit(OperSysSmsAreaDTO dto) {
    SysSmsArea sysSmsArea = sysSmsAreaConvert.toEntity(dto);
    if (sysSmsArea.getId() != null && sysSmsArea.getId() > 0) {
      if (!sysSmsAreaService.updateById(sysSmsArea)) {
        throw new ServiceException("更新区号配置失败!");
      }
    } else {
      sysSmsArea.setStatus(TrueFalse.TRUE.getValue());
      if (!sysSmsAreaService.save(sysSmsArea)) {
        throw new ServiceException("新增区号配置失败");
      }
    }
  }

  @Override
  public void smsAreaDelete(Long id) {
    if (!sysSmsAreaService.removeById(id)) {
      throw new ServiceException("删除区号配置失败!");
    }
  }

  @Override
  public EmailConfig findEmailConfig() {
    return configService.get(DictDataEnum.EMAIL, EmailConfig.class);
  }

  @Override
  public void updateEmail(EmailConfig config) {
    SysDictData dictData =
        dictDataService.getDictData(
            DictTypeEnum.EMAIL_CONFIG.getValue(), DictDataEnum.EMAIL.getLabel());

    if (null == dictData) {
      throw new ServiceException("邮箱配置不存在!");
    }

    dictDataService.updateDictData(
        OperDictDataDTO.builder()
            .id(dictData.getId())
            .dictLabel(dictData.getDictLabel())
            .dictType(dictData.getDictType())
            .dictValue(JsonUtils.toJson(config))
            .build());
  }
}

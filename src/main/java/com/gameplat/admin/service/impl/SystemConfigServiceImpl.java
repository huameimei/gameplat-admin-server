package com.gameplat.admin.service.impl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.convert.AgentContacaConfigConvert;
import com.gameplat.admin.model.dto.AgentContacaDTO;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.OperSystemConfigDTO;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.email.EmailSender;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.EmailConfig;
import com.gameplat.model.entity.AgentContacaConfig;
import com.gameplat.model.entity.sys.SysDictData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    boolean flag = false;
    if (CollectionUtils.isNotEmpty(agentContacaConfigList)) {
      for (AgentContacaConfig contacaConfig : agentContacaConfigList) {
        if (contacaConfig.getId().equals(dto.getId())) {
          flag = true;
          agentContacaConfig.setUpdateBy(GlobalContextHolder.getContext().getUsername());
          agentContacaConfig.setUpdateTime(String.valueOf(System.currentTimeMillis()));
          list.add(agentContacaConfig);
        } else { // 新增
          list.add(contacaConfig);
        }
      }
      if (!flag) {
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
    if (CollectionUtils.isNotEmpty(agentContacaConfigList)) {
      for (AgentContacaConfig agentContacaConfig : agentContacaConfigList) {
        if (!agentContacaConfig.getId().equals(id)) {
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
  @CacheInvalidate(name = CachedKeys.DICT_DATA_CACHE, key = "#dictType")
  public void updateConfig(String dictType, List<SysDictData> dictDataList) {
    List<SysDictData> dictDatas = dictDataService.getDictDataByType(dictType);
    dictDatas.forEach(e -> e.setIsDefault(DefaultEnums.NO.value()));
    dictDataList.forEach(e -> dictDatas.replaceAll(c -> c.getId().equals(e.getId()) ? e : c));

    Assert.isTrue(dictDataService.saveOrUpdateBatch(dictDatas), "修改失败!");
  }

  @Override
  public List<SysDictData> findList(String dictType) {
    return dictDataService.getDictDataByType(dictType);
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
  public EmailConfig findEmailConfig() {
    return configService.get(DictDataEnum.EMAIL, EmailConfig.class);
  }

  @Override
  public void updateEmail(EmailConfig config) {
    SysDictData dictData =
        Assert.notNull(
            dictDataService.getDictData(
                DictTypeEnum.EMAIL_CONFIG.getValue(), DictDataEnum.EMAIL.getLabel()),
            "邮箱配置不存在!");

    dictDataService.updateDictData(
        OperDictDataDTO.builder()
            .id(dictData.getId())
            .dictLabel(dictData.getDictLabel())
            .dictType(dictData.getDictType())
            .dictValue(JsonUtils.toJson(config))
            .build());
  }

  @Async
  @Override
  public void testSendEmail(EmailTestDTO dto) {
    EmailSender sender = EmailSender.buildWithConfig(this.findEmailConfig());
    sender.send(dto.getSubject(), dto.getContent(), dto.getTo());
  }
}

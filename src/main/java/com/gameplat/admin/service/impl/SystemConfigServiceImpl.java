package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.convert.AgentContacaConfigConvert;
import com.gameplat.admin.model.dto.AgentContacaDTO;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.DefaultEnums;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.lang.Assert;
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
    String dictData = configService.getValue(DictDataEnum.AGENT_CONTACT);
    if (StringUtils.isNotBlank(dictData)) {
      return JSONArray.parseArray(dictData, AgentContacaVO.class);
    }

    return Collections.emptyList();
  }

  @Override
  public void updateAgentContaca(AgentContacaDTO dto) {
    SysDictData dictData =
        dictDataService.getDictData(
            DictTypeEnum.AGENT_CONTACT_CONFIG.getValue(), DictDataEnum.AGENT_CONTACT.getLabel());

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
            DictTypeEnum.AGENT_CONTACT_CONFIG.getValue(), DictDataEnum.AGENT_CONTACT.getLabel());

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
    List<SysDictData> dictData = dictDataService.getDictDataByType(dictType);
    dictData.forEach(
        e -> {
          e.setIsDefault(DefaultEnums.NO.value());
          SysDictData entity =
              dictDataList.stream().filter(c -> c.getId().equals(e.getId())).findAny().orElse(e);
          Assert.isTrue(dictDataService.updateById(entity), "修改失败!");
        });
  }

  @Override
  public List<SysDictData> findList(String dictType) {
    return dictDataService.getDictDataByType(dictType);
  }

  @Override
  public void updateConfig(String type, Map<String, Object> params) {
    params.forEach(
        (k, v) ->
            dictDataService.updateByTypeAndLabel(
                SysDictData.builder()
                    .dictType(type)
                    .dictLabel(k)
                    .dictValue(Optional.ofNullable(v).map(Object::toString).orElse(null))
                    .build()));
  }

  @Async
  @Override
  public void testSendEmail(EmailTestDTO dto) {
    // EmailSender sender = EmailSender.buildWithConfig(this.findEmailConfig());
    // sender.send(dto.getSubject(), dto.getContent(), dto.getTo());
  }
}

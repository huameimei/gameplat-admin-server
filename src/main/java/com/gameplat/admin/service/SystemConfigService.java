package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.AgentContactDTO;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.model.entity.sys.SysDictData;

import java.util.List;
import java.util.Map;

public interface SystemConfigService {

  void updateAgentContact(AgentContactDTO agentContacaDTO);

  void delAgentContact(String id);

  List<SysDictData> findList(String dictType);

  void updateConfig(String dictType, List<SysDictData> dictDataList);

  void updateConfig(String type, Map<String, Object> params);

  void testSendEmail(EmailTestDTO dto);
}

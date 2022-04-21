package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.AgentContacaDTO;
import com.gameplat.admin.model.dto.EmailTestDTO;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.model.entity.sys.SysDictData;

import java.util.List;
import java.util.Map;

public interface SystemConfigService {

  List<AgentContacaVO> findAgentContacaList();

  void updateAgentContaca(AgentContacaDTO agentContacaDTO);

  void delAgentContaca(Long id);

  List<SysDictData> findList(String dictType);

  void updateConfig(String dictType, List<SysDictData> dictDataList);

  void updateConfig(String type, Map<String, Object> params);

  void testSendEmail(EmailTestDTO dto);
}

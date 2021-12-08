package com.gameplat.admin.service;

import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.dto.OperLiveRebateConfigDTO;
import java.util.List;

public interface LiveRebateConfigService {

  List<LiveRebateConfig> queryAll(String userLevel);

  LiveRebateConfig getById(Long id);

  void addLiveRebateConfig(OperLiveRebateConfigDTO dto);

  void updateLiveRebateConfig(OperLiveRebateConfigDTO dto);

  void delete(Long id);
}

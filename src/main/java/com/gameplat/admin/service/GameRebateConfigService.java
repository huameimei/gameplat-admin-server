package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.model.entity.game.GameRebateConfig;

import java.util.List;

public interface GameRebateConfigService {

  List<GameRebateConfig> queryAll(String userLevel);

  GameRebateConfig getById(Long id);

  void addGameRebateConfig(OperGameRebateConfigDTO dto);

  void updateGameRebateConfig(OperGameRebateConfigDTO dto);

  void delete(Long id);
}

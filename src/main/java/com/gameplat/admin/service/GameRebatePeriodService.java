package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameRebatePeriodQueryDTO;
import com.gameplat.admin.model.dto.OperGameRebatePeriodDTO;
import com.gameplat.admin.model.vo.GameRebatePeriodVO;
import com.gameplat.model.entity.game.GameRebatePeriod;

public interface GameRebatePeriodService extends IService<GameRebatePeriod> {

  IPage<GameRebatePeriodVO> queryGameRebatePeriod(
      Page<GameRebatePeriod> page, GameRebatePeriodQueryDTO dto);

  void addGameRebatePeriod(OperGameRebatePeriodDTO dto);

  void updateGameRebatePeriod(OperGameRebatePeriodDTO dto);

  void deleteGameRebatePeriod(Long id, boolean only);

  void settle(Long id);
}

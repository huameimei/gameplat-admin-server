package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.game.GameRebateDetail;
import com.gameplat.model.entity.game.GameRebatePeriod;

import java.util.List;

public interface GameRebateDetailService extends IService<GameRebateDetail> {

  List<GameRebateDetail> gameRebateDetailByStatus(Long periodId, int status);

  void deleteByPeriodId(Long periodId);

  void createGameRebateDetail(GameRebatePeriod gameRebatePeriod);
}

package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameRebateDetail;
import com.gameplat.admin.model.domain.GameRebatePeriod;
import java.util.List;

public interface GameRebateDetailService extends IService<GameRebateDetail> {

  List<GameRebateDetail> gameRebateDetailByStatus(Long periodId, int status);

  void deleteByPeriodId(Long periodId);

  void createGameRebateDetail(GameRebatePeriod gameRebatePeriod);
}

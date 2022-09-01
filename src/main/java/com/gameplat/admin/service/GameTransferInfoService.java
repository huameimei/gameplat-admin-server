package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.model.entity.game.GameTransferInfo;

import java.math.BigDecimal;
import java.util.List;

public interface GameTransferInfoService extends IService<GameTransferInfo> {

  BigDecimal getAllGameBalance(GameRWDataReportDto dto);

  List<GameTransferInfo> getGameTransferInfoList(Long memberId);

  GameTransferInfo getGameTransferInfo(Long memberId, String platformCode);

  void insertOrUpdate(GameTransferInfo gameTransferInfo);

  void asyncUpdate(GameTransferInfo gameTransferInfo);

  void asyncQueryAndUpdate(GameTransferInfo gameTransferInfo, GameBizBean gameBizBean) throws Exception;
}
